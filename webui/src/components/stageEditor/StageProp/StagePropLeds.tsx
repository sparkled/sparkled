import * as PIXI from 'pixi.js'
import React, { useEffect, useState } from 'react'
import { Point } from 'svg-path-properties'
import * as StagePropPart from './StagePropPart'
import { subscribe, unsubscribe } from '../../../utils/eventBus'

const ledRadius = 3

type RenderedStagePropData = {
  data: number[]
  ledCount: number
}

type RenderedFrameData = {
  playbackFrame: number
  renderData: {
    startFrame: number
    frameCount: number
    stageProps: Record<string, RenderedStagePropData>
  }
}

type Props = {
  /** The parent container that manages the stage prop. */
  parent: PIXI.Container

  /** The UUID of the stage prop. */
  id: string

  /** The width of the stage prop in pixels, accounting for scale. */
  width: number

  /** The height of the stage prop in pixels, accounting for scale. */
  height: number

  /** A series of coordinates used to form the stage prop line. */
  points: Point[]
}

const StagePropLeds: React.FC<Props> = props => {
  const [leds] = useState<PIXI.Graphics>(() => new PIXI.Graphics())

  useEffect(() => {
    leds.name = StagePropPart.led.name
    leds.zIndex = StagePropPart.led.zIndex

    renderLeds(leds, props.points, 0, 0, null)
    props.parent.addChild(leds)
  }, [leds, props.parent, props.points])

  useEffect(() => {
    const callback = (data: RenderedFrameData | null) => {
      if (!data) {
        renderLeds(leds, props.points, 0, 0, null)
      } else {
        const { renderData } = data
        renderLeds(leds, props.points, renderData.startFrame, data.playbackFrame, renderData.stageProps[props.id] || null)
      }
    }

    subscribe('RENDER_DATA', callback)
    return () => unsubscribe('RENDER_DATA', callback)
  }, [leds, props.points, props.id])

  return <></>
}

function renderLeds(leds: PIXI.Graphics, points: Point[], startFrame: number, frameCount: number, data: RenderedStagePropData | null) {
  leds.clear()

  for (let i = points.length - 1; i >= 0; i--) {
    const point = points[i]
    if (data) {
      // TODO handle reversed stage props.
      const frameSize = data.ledCount * 3;
      const offset = frameSize * (frameCount - startFrame);

      const ledOffset = offset + (i * 3);
      const r = data.data[ledOffset];
      const g = data.data[ledOffset + 1];
      const b = data.data[ledOffset + 2];

      leds.beginFill((r << 16) + (g << 0x8) + b)
    } else {
      leds.beginFill(0x000000)
    }

    leds.drawCircle(point.x, point.y, ledRadius)
    leds.endFill()

    const origin = points[0]
    leds.moveTo(origin.x, origin.y)
  }

  return leds
}

export default StagePropLeds
