import { makeStyles } from '@material-ui/styles'
import React, { useCallback, useContext, useEffect, useRef, useState } from 'react'
import { RouteComponentProps } from 'react-router-dom'
import PageContainer from '../../components/PageContainer'
import StageEditor from '../../components/stageEditor/StageEditor'
import useAxiosSwr from '../../hooks/api/useAxiosSwr.ts'
import { EventBusContext } from '../../hooks/useEventBus.ts'
import {
  CircleViewModel,
  Effect,
  LiveDataClearCommand,
  LiveDataModifyCommand,
  LiveDataSubscribeCommand,
  LiveDataUnsubscribeCommand,
  StageViewModel,
  ToggleInteractiveModeCommand,
} from '../../types/viewModels.ts'
import { uniqueId } from '../../utils/idUtils.ts'
import { Button } from '@material-ui/core'
import { isEmpty } from 'lodash'

const effectNames = ['Purple', 'Rainbow', 'Glitter']
const effects: Record<typeof effectNames[number], Effect> = {
  Purple: {
    id: uniqueId(),
    type: '@sparkled/solid',
    easing: {
      type: '@sparkled/linear',
      start: 0,
      end: 100,
      args: {},
    },
    fill: {
      type: '@sparkled/single-color',
      blendMode: 'NORMAL',
      args: {
        COLOR: '#ff00ff',
      },
    },
    startFrame: 0,
    endFrame: 1,
    repetitions: 1,
    repetitionSpacing: 0,
    args: {},
    targetPixels: [],
  },
  Rainbow: {
    id: uniqueId(),
    type: '@sparkled/solid',
    easing: {
      type: '@sparkled/linear',
      start: 0,
      end: 100,
      args: {},
    },
    fill: {
      type: '@sparkled/rainbow',
      blendMode: 'NORMAL',
      args: {
        CYCLES_PER_SECOND: '.5',
      },
    },
    startFrame: 0,
    endFrame: 1,
    repetitions: 1,
    repetitionSpacing: 0,
    args: {},
    targetPixels: [],
  },
  Glitter: {
    id: uniqueId(),
    type: '@sparkled/glitter',
    easing: {
      type: '@sparkled/linear',
      start: 0,
      end: 100,
      args: {},
    },
    fill: {
      type: '@sparkled/single-color',
      blendMode: 'NORMAL',
      args: {
        COLOR: '#00ff00',
        DENSITY: '100',
        LIFETIME: '30',
      },
    },
    startFrame: 0,
    endFrame: 1,
    repetitions: 1,
    repetitionSpacing: 0,
    args: {},
  },
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
  buttonRow: {
    position: 'absolute',
    bottom: '0',
    width: '100%',
    display: 'flex',
    flexWrap: 'wrap',
    gap: '4px',
    padding: '4px',
    alignItems: 'center',
  },
  activeEffect: {
    fontWeight: 'bold',
    background: '#ffffff33',
  },
}))

type Props = RouteComponentProps<{ stageId: string }>

const StageLivePaintPage: React.FC<Props> = props => {
  const classes = useStyles()
  const eventBus = useContext(EventBusContext)
  const [effectName, setEffectName] = useState(effectNames[0])
  const pendingCircles = useRef<CircleViewModel[]>([])

  const { data } = useAxiosSwr<StageViewModel>({ url: `/stages/${props.match.params.stageId}` }, true)

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
      const circles = pendingCircles.current
      if (!isEmpty(circles)) {
        eventBus.sendWebSocketCommand<LiveDataModifyCommand>({
          type: 'LDM',
          p: circles,
          e: effects[effectName],
        })
        pendingCircles.current = []
      }
    }, 50)

    return () => clearInterval(interval)
  }, [effectName, eventBus])

  return (
    <PageContainer className={classes.pageContainer} spacing={0}>
      <div className={classes.container}>
        {data && (
          <StageEditor
            stage={data}
            onTouchMove={(x, y) => {
              pendingCircles.current = [...pendingCircles.current, { x, y, r: 30 }]
            }}
          />
        )}

        <div className={classes.buttonRow}>
          {effectNames.map(it => (
            <Button
              variant='outlined'
              className={effectName === it ? classes.activeEffect : undefined}
              onClick={() => setEffectName(it)}
            >
              {it}
            </Button>
          ))}
          <Button onClick={clearLiveData} color='secondary'>
            Clear
          </Button>
        </div>
      </div>
    </PageContainer>
  )
}

export default StageLivePaintPage
