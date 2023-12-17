import { makeStyles } from '@material-ui/styles'
import React, { useCallback, useContext, useEffect, useRef, useState } from 'react'
import { RouteComponentProps } from 'react-router-dom'
import PageContainer from '../../components/PageContainer'
import StageEditor from '../../components/stageEditor/StageEditor'
import useAxiosSwr from '../../hooks/api/useAxiosSwr.ts'
import { EventBusContext } from '../../hooks/useEventBus.ts'
import {
  Effect,
  Fill,
  LiveDataClearCommand,
  LiveDataModifyCommand,
  LiveDataSubscribeCommand,
  LiveDataUnsubscribeCommand,
  Point2dViewModel,
  StageViewModel,
  ToggleInteractiveModeCommand,
} from '../../types/viewModels.ts'
import { uniqueId } from '../../utils/idUtils.ts'
import { Button } from '@material-ui/core'
import { isEmpty, pickBy } from 'lodash'

const effectNames = [
  'Red',
  'Green',
  'Blue',
  'Yellow',
  'Purple',
  'Rainbow',
  'Green Glitter',
  'Purple Glitter',
  'Pink Glitter',
  'Gold Glitter',
  'Erase',
]

const allEffects: Record<typeof effectNames[number], Effect> = {
  Red: solid(singleColorFill('#ff0000')),
  Green: solid(singleColorFill('#00ff00')),
  Blue: solid(singleColorFill('#0000ff')),
  Yellow: solid(singleColorFill('#ffff00')),
  Gold: solid(singleColorFill('#ffcc33')),
  Purple: solid(singleColorFill('#781ffd')),
  Pink: solid(singleColorFill('#ff06c6')),
  White: solid(singleColorFill('#ffffff')),
  Black: solid(singleColorFill('#000000')),
  Rainbow: solid(rainbowFill()),

  'Red Glitter': glitter(singleColorFill('#ff0000'), 1),
  'Green Glitter': glitter(singleColorFill('#00ff00'), 2),
  'Blue Glitter': glitter(singleColorFill('#0000ff'), 3),
  'Yellow Glitter': glitter(singleColorFill('#ffff00'), 4),
  'Gold Glitter': glitter(singleColorFill('#ffcc33'), 5),
  'Purple Glitter': glitter(singleColorFill('#781ffd'), 6),
  'Pink Glitter': glitter(singleColorFill('#ff06c6'), 7),
  'White Glitter': glitter(singleColorFill('#ffffff'), 8),
  'Black Glitter': glitter(singleColorFill('#000000'), 9),
  'Rainbow Glitter': glitter(rainbowFill(), 10),
}

const solidEffects: Record<typeof effectNames[number], Effect> = pickBy(allEffects, it => it.type === '@sparkled/solid')
const glitterEffects: Record<typeof effectNames[number], Effect> = pickBy(
  allEffects,
  it => it.type === '@sparkled/glitter'
)
const otherEffects: Record<typeof effectNames[number], Effect> = pickBy(
  allEffects,
  it => it.type !== '@sparkled/solid' && it.type !== '@sparkled/glitter'
)

function solid(fill: Fill): Effect {
  return {
    id: uniqueId(),
    type: '@sparkled/solid',
    easing: {
      type: '@sparkled/linear',
      start: 0,
      end: 100,
      args: {},
    },
    fill,
    startFrame: 0,
    endFrame: 1,
    repetitions: 1,
    repetitionSpacing: 0,
    args: {},
  }
}

function glitter(fill: Fill, seed: number): Effect {
  return {
    id: uniqueId(),
    type: '@sparkled/glitter',
    easing: {
      type: '@sparkled/linear',
      start: 0,
      end: 100,
      args: {},
    },
    fill,
    startFrame: 0,
    endFrame: 1,
    repetitions: 1,
    repetitionSpacing: 0,
    args: {
      SEED: [seed.toString()],
    },
  }
}

function singleColorFill(color: string): Fill {
  return {
    type: '@sparkled/single-color',
    blendMode: 'NORMAL',
    args: {
      COLOR: [color],
    },
  }
}

function rainbowFill(): Fill {
  return {
    type: '@sparkled/rainbow',
    blendMode: 'NORMAL',
    args: {
      CYCLES_PER_SECOND: ['.5'],
    },
  }
}

function gradientFill(colors: string[], repetitions: number, hardness: number, cyclesPerSecond: number): Fill {
  return {
    type: '@sparkled/gradient',
    blendMode: 'NORMAL',
    args: {
      COLORS: colors,
      COLOR_REPETITIONS: [repetitions.toString()],
      BLEND_HARDNESS: [hardness.toString()],
      CYCLES_PER_SECOND: [cyclesPerSecond.toString()],
    },
  }
}

const useStyles = makeStyles(() => ({
  // The page container never overflows, and the editor tools need to be hidden when they slide offscreen.
  pageContainer: {
    overflow: 'hidden',
  },
  container: {
    display: 'flex',
    justifyContent: 'center',
    width: '100%',
    height: '100%',
    overflow: 'hidden',
  },
  effectContainer: {
    position: 'absolute',
    bottom: '0',
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
    padding: '4px',
    alignItems: 'center',
    justifyContent: 'center',
  },
  effectContainerInner: {
    display: 'flex',
    flexDirection: 'column',
    gap: '4px',
  },
  effectRow: {
    display: 'flex',
    flexWrap: 'wrap',
    gap: '4px',
    width: 'max-content',
    alignItems: 'center',
    justifyContent: 'start',
  },
  effectRowLabel: {
    minWidth: '60px',
    textAlign: 'end',
    paddingRight: '8px',
  },
  activeEffect: {
    background: '#ffffff33',
  },
}))

type Props = RouteComponentProps<{ stageId: string }>

const StageLivePaintPage: React.FC<Props> = props => {
  const classes = useStyles()
  const eventBus = useContext(EventBusContext)
  const [effectName, setEffectName] = useState(effectNames[0])
  const pendingTouchPoints = useRef<Point2dViewModel[]>([])
  const touchRadius = 30

  const { data: stage } = useAxiosSwr<StageViewModel>({ url: `/stages/${props.match.params.stageId}` }, true)

  useEffect(() => {
    const interval = setInterval(() => {
      eventBus.sendWebSocketCommand<LiveDataSubscribeCommand>({ type: 'LDS' })
    }, 2000)

    return () => {
      clearInterval(interval)
      eventBus.sendWebSocketCommand<LiveDataUnsubscribeCommand>({ type: 'LDU' })
    }
  }, [eventBus])

  useEffect(() => {
    const interval = setInterval(() => {
      eventBus.sendWebSocketCommand<ToggleInteractiveModeCommand>({
        type: 'TIM',
        enabled: true,
        stageId: props.match.params.stageId,
      })
    }, 2000)

    return () => {
      clearInterval(interval)
      eventBus.sendWebSocketCommand<ToggleInteractiveModeCommand>({
        type: 'TIM',
        enabled: false,
      })
    }
  }, [eventBus, props.match.params.stageId])

  const clearLiveData = useCallback(() => {
    eventBus.sendWebSocketCommand<LiveDataClearCommand>({
      type: 'LDC',
    })
  }, [eventBus])

  useEffect(() => {
    const interval = setInterval(() => {
      const touchPoints = pendingTouchPoints.current
      if (!isEmpty(touchPoints)) {
        eventBus.sendWebSocketCommand<LiveDataModifyCommand>({
          type: 'LDM',
          tp: touchPoints,
          d: 30,
          e: allEffects[effectName],
        })
        pendingTouchPoints.current = []
      }
    }, 100)

    return () => clearInterval(interval)
  }, [effectName, eventBus])

  const onTouchMove = useCallback(
    (x: number, y: number) => {
      if (stage != null) {
        if (
          x >= -touchRadius &&
          x <= stage.width + touchRadius &&
          y >= -touchRadius &&
          y <= stage.height + touchRadius
        ) {
          pendingTouchPoints.current = [...pendingTouchPoints.current, { x, y }]
        }
      }
    },
    [stage]
  )

  return (
    <PageContainer className={classes.pageContainer} spacing={0}>
      <div className={classes.container}>
        {stage && <StageEditor stage={stage} onTouchMove={onTouchMove} />}

        <div className={classes.effectContainer}>
          <div className={classes.effectContainerInner}>
            <div className={classes.effectRow}>
              <div className={classes.effectRowLabel}>Paint:</div>
              {Object.keys(solidEffects).map(it => (
                <Button
                  key={it}
                  variant='outlined'
                  className={effectName === it ? classes.activeEffect : undefined}
                  onClick={() => setEffectName(it)}
                >
                  {it}
                </Button>
              ))}
            </div>

            <div className={classes.effectRow}>
              <div className={classes.effectRowLabel}>Glitter:</div>
              {Object.keys(glitterEffects).map(it => (
                <Button
                  key={it}
                  variant='outlined'
                  className={effectName === it ? classes.activeEffect : undefined}
                  onClick={() => setEffectName(it)}
                >
                  {it.replace(' Glitter', '')}
                </Button>
              ))}
            </div>

            <div className={classes.effectRow}>
              <div className={classes.effectRowLabel}>Other:</div>
              {Object.keys(otherEffects).map(it => (
                <Button
                  key={it}
                  variant='outlined'
                  className={effectName === it ? classes.activeEffect : undefined}
                  onClick={() => setEffectName(it)}
                >
                  {it}
                </Button>
              ))}
              <Button onClick={clearLiveData} variant='contained'>
                Clear
              </Button>
            </div>
          </div>
        </div>
      </div>
    </PageContainer>
  )
}

export default StageLivePaintPage
