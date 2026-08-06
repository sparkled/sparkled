import * as PIXI from 'pixi.js'
import React, { useContext, useEffect, useState } from 'react'
import { Point } from 'svg-path-properties'
import { EventBusContext } from '../../../hooks/useEventBus.ts'
import { LiveDataResponseCommand, StagePropViewModel } from '../../../types/viewModels.ts'
import { subscribe, unsubscribe } from '../../../utils/eventBus'
import * as StagePropPart from './StagePropPart'

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

  stageProp: StagePropViewModel

  /** The width of the stage prop in pixels, accounting for scale. */
  width: number

  /** The height of the stage prop in pixels, accounting for scale. */
  height: number

  /** A series of coordinates used to form the stage prop line. */
  points: Point[]
}

const StagePropLeds: React.FC<Props> = props => {
  const [leds] = useState<PIXI.Graphics>(() => new PIXI.Graphics())
  const eventBus = useContext(EventBusContext)

  useEffect(() => {
    const listener = eventBus.addListener('responseReceived', async command => {
      if (command.type === 'LDR') {
        const { data } = command as LiveDataResponseCommand
        const propData = data[props.stageProp.groupCode ?? props.stageProp.code]

        renderLeds(leds, props.points, 0, 0, props.stageProp.ledOffset, {
          data: propData,
          ledCount: props.stageProp.ledCount,
        })
      }
    })

    return () => eventBus.removeListener(listener)
  }, [
    eventBus,
    leds,
    props.stageProp.code,
    props.points,
    props.stageProp.ledCount,
    props.stageProp.groupCode,
    props.stageProp.ledOffset,
  ])

  useEffect(() => {
    leds.name = StagePropPart.led.name
    leds.zIndex = StagePropPart.led.zIndex

    renderLeds(leds, props.points, 0, 0, 0, null)
    props.parent.addChild(leds)
  }, [leds, props.parent, props.points])

  useEffect(() => {
    const callback = (data: RenderedFrameData | null) => {
      if (!data) {
        renderLeds(leds, props.points, 0, 0, 0, null)
      } else {
        const { renderData } = data
        renderLeds(
          leds,
          props.points,
          renderData.startFrame,
          data.playbackFrame,
          0,
          renderData.stageProps[props.stageProp.code] || null
        )
      }
    }

    subscribe('RENDER_DATA', callback)
    return () => {
      return unsubscribe('RENDER_DATA', callback)
    }
  }, [leds, props.points, props.stageProp.code, props.stageProp.groupCode, props.stageProp.id])

  return <></>
}

function renderLeds(
  leds: PIXI.Graphics,
  points: Point[],
  startFrame: number,
  frameCount: number,
  groupLedOffset: number,
  data: RenderedStagePropData | null
) {
  leds.clear()
  for (let i = points.length - 1; i >= 0; i--) {
    const point = points[i]
    if (data && data.data) {
      // TODO handle reversed stage props.
      const frameSize = data.ledCount * 3
      const offset = groupLedOffset * 3 + frameSize * (frameCount - startFrame)

      const ledOffset = offset + i * 3
      const r = data.data[ledOffset]
      const g = data.data[ledOffset + 1]
      const b = data.data[ledOffset + 2]

      leds.beginFill((r << 16) + (g << 8) + b)
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
