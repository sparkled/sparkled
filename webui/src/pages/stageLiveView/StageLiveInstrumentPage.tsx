import { makeStyles } from '@material-ui/styles'
import React, { useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { RouteComponentProps } from 'react-router-dom'
import PageContainer from '../../components/PageContainer'
import StageEditor from '../../components/stageEditor/StageEditor'
import useAxiosSwr from '../../hooks/api/useAxiosSwr.ts'
import { EventBusContext } from '../../hooks/useEventBus.ts'
import {
  Effect,
  Fill,
  LiveDataModifyCommand,
  LiveDataSubscribeCommand,
  LiveDataUnsubscribeCommand,
  PlayAudioFileCommand,
  StageViewModel,
  ToggleInteractiveModeCommand,
} from '../../types/viewModels.ts'
import { uniqueId } from '../../utils/idUtils.ts'

type InstrumentKey = {
  songId: string
  effectFn: () => Effect
}

const instrumentsConfig: Record<
  string,
  [
    InstrumentKey,
    InstrumentKey,
    InstrumentKey,
    InstrumentKey,
    InstrumentKey,
    InstrumentKey,
    InstrumentKey,
    InstrumentKey
  ]
> = {
  Drum: [
    { songId: 'jdhyw9y2r2ht', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhywbv5w3cf', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhywcw96yy6', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhywdssmqrs', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhywfjrvv5y', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhywg9x4hzn', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhywh4qfvs6', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhywhy5rg2z', effectFn: () => flash(singleColorFill('#ff0000')) },
  ],
  Harp: [
    { songId: 'jdhyvc2hddsx', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvcyyssgd', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvdx4975c', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvfzw3k8c', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvgtb2j8s', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvhkz8q29', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvjnptq2z', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvkk8vw6z', effectFn: () => flash(singleColorFill('#ff0000')) },
  ],
  Glockenspiel: [
    { songId: 'jdhyvxvsc64y', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvyxn3zkk', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyw24tq8p5', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyw3jqyfrk', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyw4p56yxw', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyw5rhn6w6', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyw6qx5qx6', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyw7xmbc8s', effectFn: () => flash(singleColorFill('#ff0000')) },
  ],
  Guitar: [
    { songId: 'jdhyvnp3fssj', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvpp66yjy', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvqfsptvd', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvr92krxt', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvs2fjvvd', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvsr9k6v4', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvtp2rvjc', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyvvm555wn', effectFn: () => flash(singleColorFill('#ff0000')) },
  ],
  Piano: [
    { songId: 'jdhyv2fs6cqk', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv3b3wf5n', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv4524cdy', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv4wpn66q', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv5zxj9ry', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv6xbwhqx', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv7zcchts', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv93jnmb3', effectFn: () => flash(singleColorFill('#ff0000')) },
  ],
}

const instrumentNames = Object.keys(instrumentsConfig)

const colors = ['#ff0000', '#00ff00', '#ffff00', '#ffcc33', '#781ffd', '#ff06c6', '#ff00ff']

// const allEffects: Record<typeof effectNames[number], Effect> = {
//   Red: solid(singleColorFill('#ff0000')),
//   Green: solid(singleColorFill('#00ff00')),
//   Blue: solid(singleColorFill('#0000ff')),
//   Yellow: solid(singleColorFill('#ffff00')),
//   Gold: solid(singleColorFill('#ffcc33')),
//   Purple: solid(singleColorFill('#781ffd')),
//   Pink: solid(singleColorFill('#ff06c6')),
//   White: solid(singleColorFill('#ffffff')),
//   Black: solid(singleColorFill('#000000')),
//   Rainbow: solid(rainbowFill()),
//
//   'Red Glitter': glitter(1, singleColorFill('#ff0000')),
//   'Green Glitter': glitter(2, singleColorFill('#00ff00')),
//   'Blue Glitter': glitter(3, singleColorFill('#0000ff')),
//   'Yellow Glitter': glitter(4, singleColorFill('#ffff00')),
//   'Gold Glitter': glitter(5, singleColorFill('#ffcc33')),
//   'Purple Glitter': glitter(6, singleColorFill('#781ffd')),
//   'Pink Glitter': glitter(7, singleColorFill('#ff06c6')),
//   'White Glitter': glitter(8, singleColorFill('#ffffff')),
//   'Black Glitter': glitter(9, singleColorFill('#000000')),
//   'Rainbow Glitter': glitter(10, rainbowFill()),
// }

function flash(fill: Fill): Effect {
  return {
    id: uniqueId(),
    type: 'sparkled:flash:1.0.0',
    easing: {
      type: 'sparkled:linear:1.0.0',
      start: 0,
      end: 100,
      args: {},
    },
    fill,
    startFrame: 0,
    endFrame: 15,
    repetitions: 1,
    repetitionSpacing: 0,
    args: {},
  }
}

function glitter(fill: Fill): Effect {
  return {
    id: uniqueId(),
    type: 'sparkled:glitter:1.0.0',
    easing: {
      type: 'sparkled:linear:1.0.0',
      start: 0,
      end: 100,
      args: {},
    },
    fill,
    startFrame: 0,
    endFrame: 30,
    repetitions: 1,
    repetitionSpacing: 0,
    args: {
      randomSeed: ['∴kts', `System.currentTimeMillis() + stageProp.id.hashCode()`],
      density: ['0.1'],
    },
  }
}

function singleColorFill(color: string): Fill {
  return {
    type: 'sparkled:single-color:1.0.0',
    blendMode: 'NORMAL',
    args: {
      color: [color],
    },
  }
}

function rainbowFill(): Fill {
  return {
    type: 'sparkled:rainbow:1.0.0',
    blendMode: 'NORMAL',
    args: {
      cyclesPerSecond: ['.5'],
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
  instrumentSelect: {
    position: 'absolute',
    top: '8px',
    right: '8px',
  },
  instrumentKeyRow: {
    display: 'flex',
  },
  instrumentKey: {
    display: 'flex',
    width: '100px',
    height: '100px',
    alignItems: 'center',
    justifyContent: 'center',
    borderTop: '1px solid rgba(255, 255, 255, .1)',
    borderRight: '1px solid rgba(255, 255, 255, .1)',
    borderBottom: '1px solid rgba(255, 255, 255, .1)',
    '&:first-of-type': {
      borderLeft: '1px solid rgba(255, 255, 255, .1)',
    },
  },
  activeInstrumentKey: {
    background: 'rgba(255, 255, 255, .1)',
  },
}))

type Props = RouteComponentProps<{ stageId: string }>

const StageLiveInstrumentPage: React.FC<Props> = props => {
  const classes = useStyles()
  const eventBus = useContext(EventBusContext)
  const [instrumentName, setInstrumentName] = useState('Piano')

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

  return (
    <PageContainer className={classes.pageContainer} spacing={0}>
      <div className={classes.container}>
        {stage && <StageEditor stage={stage} />}

        <select
          className={classes.instrumentSelect}
          value={instrumentName}
          onChange={e => setInstrumentName(e.target.value as string)}
        >
          {instrumentNames.map(it => (
            <option key={it}>{it}</option>
          ))}
        </select>

        <div className={classes.effectContainer}>
          <div className={classes.effectContainerInner}>
            {stage && <InstrumentButtonRow instrumentKeys={instrumentsConfig[instrumentName]} stage={stage} />}
          </div>
        </div>
      </div>
    </PageContainer>
  )
}

const InstrumentButtonRow: React.FC<{ instrumentKeys: InstrumentKey[]; stage: StageViewModel }> = ({
  instrumentKeys,
  stage,
}) => {
  const classes = useStyles()
  const [pressed, setPressed] = useState(false)

  const instrumentButtons = useMemo(() => {
    return instrumentKeys.map((instrumentKey, index) => (
      <InstrumentButton
        key={instrumentKey.songId}
        keyIndex={index}
        songId={instrumentKey.songId}
        effectFn={instrumentKey.effectFn}
        pressed={pressed}
        stage={stage}
      />
    ))
  }, [instrumentKeys, pressed, stage])

  return (
    <div
      className={classes.instrumentKeyRow}
      onMouseDown={() => {
        setPressed(true)
        console.info('pressedtrue=')
      }}
      onMouseUp={() => {
        setPressed(false)
        console.info('pressed=false')
      }}
      onTouchStart={() => {
        setPressed(true)
        console.info('pressed=true')
      }}
      onTouchEnd={() => {
        setPressed(false)
        console.info('pressed=false')
      }}
    >
      {instrumentButtons}
    </div>
  )
}

const InstrumentButton: React.FC<InstrumentKey & { pressed: boolean; stage: StageViewModel; keyIndex: number }> = ({
  effectFn,
  keyIndex,
  pressed,
  songId,
  stage,
}) => {
  const classes = useStyles()
  const eventBus = useContext(EventBusContext)

  const topLeft = { x: (stage.width / 8) * keyIndex, y: 0 }
  const bottomRight = { x: (stage.width / 8) * (keyIndex + 1), y: stage.height }

  const [active, setActive] = useState(false)
  const onPress = useCallback(() => {
    console.info('onPress', pressed, active)
    if (pressed && !active) {
      setActive(true)
      eventBus.sendWebSocketCommand<LiveDataModifyCommand>({
        type: 'LDM',
        me: false,
        tp: [topLeft, bottomRight],
        d: 30,
        e: effectFn(),
        st: 'BOX',
      })

      setTimeout(() => {
        eventBus.sendWebSocketCommand<PlayAudioFileCommand>({
          type: 'PAF',
          id: songId,
        })
      }, 100)
    }
  }, [active, bottomRight, effectFn, eventBus, pressed, songId, topLeft])

  const onRelease = useCallback(() => {
    setActive(false)
  }, [])

  return (
    <div
      className={`${classes.instrumentKey} ${active ? classes.activeInstrumentKey : ''}`}
      onMouseOver={onPress}
      onMouseOut={onRelease}
      onTouchStart={onPress}
      onTouchEnd={onRelease}
    >
      M
    </div>
  )
}

export default StageLiveInstrumentPage
