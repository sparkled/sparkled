import _, { maxBy, minBy } from 'lodash'
import * as PIXI from 'pixi.js'
import React, { useCallback, useContext, useEffect, useState } from 'react'
import { Point, svgPathProperties } from 'svg-path-properties'
import stagePropTypes from '../../../data/stagePropTypes'
import { Rectangle, StagePropViewModel } from '../../../types/viewModels.ts'
import Logger from '../../../utils/Logger'
import { StageEditorDispatchContext } from '../StageEditorReducer'
import StagePropBackground from './StagePropBackground'
import StagePropPath from './StagePropPath'
import StagePropRotateHandle from './StagePropRotateHandle'
import StagePropLeds from './StagePropLeds'
import { getLinePoints } from '../../../utils/stagePropUtils'
import { IPoint } from 'pixi.js'

const logger = new Logger('StageProp')

interface Props {
  /** The PIXI application used to render the stage canvas. */
  pixiApp: PIXI.Application

  /** The stage prop being displayed or edited. */
  stageProp: StagePropViewModel

  /** Whether to enable stage prop editing. */
  editable: boolean
}

interface State {
  /** The PIXI container that holds the stage prop. */
  pixiContainer: PIXI.Container

  /** The width of the unscaled stage prop. */
  width: number

  /** The height of the unscaled stage prop. */
  height: number

  /** The scaled points of the path that makes up the stage prop. */
  pathPoints: Point[]

  /** The position of the LEDs on the stage prop. */
  ledPoints: Point[]
}

const StageProp: React.FC<Props> = props => {
  const { stageProp } = props

  const [state] = useState<State>(() => initState(props.pixiApp, stageProp))
  const dispatch = useContext(StageEditorDispatchContext)
  const { pixiContainer, width, height, pathPoints, ledPoints } = state

  const selectStageProp = useCallback(() => {
    dispatch({ type: 'SelectStageProp', payload: { id: stageProp.id } })
  }, [dispatch, stageProp.id])

  const setPosition = useCallback(() => {
    pixiContainer.x = stageProp.positionX + pixiContainer.width / 2
    pixiContainer.y = stageProp.positionY + pixiContainer.height / 2
  }, [pixiContainer, stageProp.positionX, stageProp.positionY])

  const setRotation = useCallback(() => {
    pixiContainer.rotation = (stageProp.rotation / 180) * Math.PI
  }, [pixiContainer, stageProp.rotation])

  setPosition()
  useEffect(setPosition, [pixiContainer, stageProp.positionX, stageProp.positionY])

  setRotation()
  useEffect(setRotation, [pixiContainer, stageProp.rotation])

  useEffect(() => {
    const gridPos = props.pixiApp.stage.position
    const { x: scaleX, y: scaleY } = props.pixiApp.stage.scale

    const points = ledPoints
      .map(it => pixiContainer.toGlobal(it as IPoint))
      .map(it => ({ x: (it.x - gridPos.x) / scaleX, y: (it.y - gridPos.y) / scaleY }))
    const bounds: Rectangle = {
      x1: minBy(points, it => it.x)?.x ?? 0,
      y1: minBy(points, it => it.y)?.y ?? 0,
      x2: maxBy(points, it => it.x)?.x ?? 0,
      y2: maxBy(points, it => it.y)?.y ?? 0,
    }
    dispatch({ type: 'UpdateStagePropLedPositions', payload: { ledPositions: { bounds, points } } })
  }, [dispatch, ledPoints, pixiContainer, props.pixiApp.stage, stageProp])

  useEffect(() => {
    return () => {
      logger.info(`Destroying ${stageProp.id}.`)
      state.pixiContainer.destroy({ children: true })
    }
  }, [stageProp.id, state.pixiContainer])

  const moveStageProp = useCallback(
    (offsetX: number, offsetY: number) => {
      dispatch({
        type: 'MoveStageProp',
        payload: {
          x: Math.round(stageProp.positionX + offsetX),
          y: Math.round(stageProp.positionY + offsetY),
        },
      })
    },
    [dispatch, stageProp.positionX, stageProp.positionY]
  )

  const rotateStageProp = useCallback(
    (rotation: number) => {
      dispatch({
        type: 'RotateStageProp',
        payload: { rotation: Math.round(rotation) },
      })
    },
    [dispatch]
  )

  return (
    <>
      <StagePropBackground
        parent={pixiContainer}
        width={width}
        height={height}
        editable={props.editable}
        onClicked={selectStageProp}
        onMoved={moveStageProp}
      />

      <StagePropPath parent={pixiContainer} points={pathPoints} width={width} height={height} />

      <StagePropLeds parent={pixiContainer} stageProp={stageProp} points={ledPoints} width={width} height={height} />

      <StagePropRotateHandle
        parent={pixiContainer}
        width={width}
        editable={props.editable}
        onClicked={selectStageProp}
        onRotated={rotateStageProp}
      />
    </>
  )
}

function initState(pixiApp: PIXI.Application, stageProp: StagePropViewModel): State {
  const { path } = stagePropTypes[stageProp.type!]
  const pathProperties = svgPathProperties(path)

  const pathPoints = getLinePoints(pathProperties, stageProp)
  const ledPoints = getLinePoints(pathProperties, stageProp, stageProp.ledCount)
  const width = _.maxBy(pathPoints, 'x')!.x
  const height = _.maxBy(pathPoints, 'y')!.y

  const pixiContainer = buildContainer(width, height)
  pixiApp.stage.addChild(pixiContainer)

  return { pixiContainer, pathPoints, ledPoints, width, height }
}

function buildContainer(width: number, height: number) {
  const pixiContainer = new PIXI.Container()
  pixiContainer.sortableChildren = true
  pixiContainer.scale.x = pixiContainer.scale.y = 1
  pixiContainer.pivot.x = width / 2
  pixiContainer.pivot.y = height / 2
  return pixiContainer
}

export default StageProp
