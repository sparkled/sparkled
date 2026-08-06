import { makeStyles } from '@material-ui/styles'
import { MusicNoteRounded } from '@material-ui/icons'
import React, { useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react'
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
    { songId: 'jdhyw9y2r2ht', effectFn: () => splitLine(singleColorFill('#ff0000')) },
    { songId: 'jdhywbv5w3cf', effectFn: () => splitLine(singleColorFill('#ff6600')) },
    { songId: 'jdhywcw96yy6', effectFn: () => splitLine(singleColorFill('#ffff00')) },
    { songId: 'jdhywdssmqrs', effectFn: () => splitLine(singleColorFill('#00ff00')) },
    { songId: 'jdhywfjrvv5y', effectFn: () => splitLine(singleColorFill('#0000ff')) },
    { songId: 'jdhywg9x4hzn', effectFn: () => splitLine(singleColorFill('#00ccff')) },
    { songId: 'jdhywh4qfvs6', effectFn: () => splitLine(singleColorFill('#781ffd')) },
    { songId: 'jdhywhy5rg2z', effectFn: () => splitLine(singleColorFill('#ff06c6')) },
  ],
  Harp: [
    { songId: 'jdhyvc2hddsx', effectFn: () => flash(rainbowFill()) },
    { songId: 'jdhyvcyyssgd', effectFn: () => flash(rainbowFill()) },
    { songId: 'jdhyvdx4975c', effectFn: () => flash(rainbowFill()) },
    { songId: 'jdhyvfzw3k8c', effectFn: () => flash(rainbowFill()) },
    { songId: 'jdhyvgtb2j8s', effectFn: () => flash(rainbowFill()) },
    { songId: 'jdhyvhkz8q29', effectFn: () => flash(rainbowFill()) },
    { songId: 'jdhyvjnptq2z', effectFn: () => flash(rainbowFill()) },
    { songId: 'jdhyvkk8vw6z', effectFn: () => flash(rainbowFill()) },
  ],
  Glockenspiel: [
    { songId: 'jdhyvxvsc64y', effectFn: () => glitter(singleColorFill('#ff0000')) },
    { songId: 'jdhyvyxn3zkk', effectFn: () => glitter(singleColorFill('#ff6600')) },
    { songId: 'jdhyw24tq8p5', effectFn: () => glitter(singleColorFill('#ffff00')) },
    { songId: 'jdhyw3jqyfrk', effectFn: () => glitter(singleColorFill('#00ff00')) },
    { songId: 'jdhyw4p56yxw', effectFn: () => glitter(singleColorFill('#0000ff')) },
    { songId: 'jdhyw5rhn6w6', effectFn: () => glitter(singleColorFill('#00ccff')) },
    { songId: 'jdhyw6qx5qx6', effectFn: () => glitter(singleColorFill('#781ffd')) },
    { songId: 'jdhyw7xmbc8s', effectFn: () => glitter(singleColorFill('#ff06c6')) },
  ],

  Guitar: [
    { songId: 'jdhyvnp3fssj', effectFn: () => line(singleColorFill('#ff0000')) },
    { songId: 'jdhyvpp66yjy', effectFn: () => line(singleColorFill('#ff6600')) },
    { songId: 'jdhyvqfsptvd', effectFn: () => line(singleColorFill('#ffff00')) },
    { songId: 'jdhyvr92krxt', effectFn: () => line(singleColorFill('#00ff00')) },
    { songId: 'jdhyvs2fjvvd', effectFn: () => line(singleColorFill('#0000ff')) },
    { songId: 'jdhyvsr9k6v4', effectFn: () => line(singleColorFill('#00ccff')) },
    { songId: 'jdhyvtp2rvjc', effectFn: () => line(singleColorFill('#781ffd')) },
    { songId: 'jdhyvvm555wn', effectFn: () => line(singleColorFill('#ff06c6')) },
  ],
  Piano: [
    { songId: 'jdhyv2fs6cqk', effectFn: () => flash(singleColorFill('#ff0000')) },
    { songId: 'jdhyv3b3wf5n', effectFn: () => flash(singleColorFill('#ff6600')) },
    { songId: 'jdhyv4524cdy', effectFn: () => flash(singleColorFill('#ffff00')) },
    { songId: 'jdhyv4wpn66q', effectFn: () => flash(singleColorFill('#00ff00')) },
    { songId: 'jdhyv5zxj9ry', effectFn: () => flash(singleColorFill('#0000ff')) },
    { songId: 'jdhyv6xbwhqx', effectFn: () => flash(singleColorFill('#00ccff')) },
    { songId: 'jdhyv7zcchts', effectFn: () => flash(singleColorFill('#781ffd')) },
    { songId: 'jdhyv93jnmb3', effectFn: () => flash(singleColorFill('#ff06c6')) },
  ],
  Animals: [
    { songId: 'jnt2hkfchjqr', effectFn: () => flash(singleColorFill('#ff6600')) }, // Cat
    { songId: 'jnt2hpm3vj6k', effectFn: () => flash(singleColorFill('#0000ff')) }, // Dog
    { songId: 'jnt2hjctvkbm', effectFn: () => flash(singleColorFill('#ff0000')) }, // Bird
    { songId: 'jnt2hnnsxz9x', effectFn: () => flash(singleColorFill('#781ffd')) }, // Cow
    { songId: 'jnt2hmkj83xh', effectFn: () => flash(singleColorFill('#ffff00')) }, // Chicken
    { songId: 'jnt2hrd4bnwn', effectFn: () => flash(singleColorFill('#00ff00')) }, // Frog
    { songId: 'jnt2hqfsfjhb', effectFn: () => flash(singleColorFill('#00ccff')) }, // Duck
    { songId: 'jnt2hs8qn6sg', effectFn: () => flash(singleColorFill('#ff06c6')) }, // Horse
  ],
}

const instrumentNames = Object.keys(instrumentsConfig)

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

function line(fill: Fill): Effect {
  return {
    id: uniqueId(),
    type: 'sparkled:line:1.0.0',
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
    args: {
      lineLength: ['∴kts', `pixelCount / 2`],
    },
  }
}

function splitLine(fill: Fill): Effect {
  return {
    id: uniqueId(),
    type: 'sparkled:split-line:1.0.0',
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
    args: {
      lineLength: ['∴kts', `pixelCount / 2`],
    },
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
      density: ['0.3'],
      lifetimeMs: ['500'],
    },
  }
}

function singleColorFill(color: string): Fill {
  return {
    type: 'sparkled:single-color:1.0.0',
    blendMode: 'ADD',
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
    padding: '12px 32px 12px 8px',
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
  const containerRef = useRef<HTMLDivElement>(null)
  const [activeIndex, setActiveIndex] = useState(-1)

  const instrumentButtons = useMemo(() => {
    return instrumentKeys.map((instrumentKey, index) => (
      <InstrumentButton
        active={activeIndex === index}
        key={instrumentKey.songId}
        keyIndex={index}
        songId={instrumentKey.songId}
        effectFn={instrumentKey.effectFn}
        stage={stage}
      />
    ))
  }, [activeIndex, instrumentKeys, stage])

  const updateActiveIndex = useCallback(
    (e: React.PointerEvent<HTMLDivElement>) => {
      e.stopPropagation()
      const { x, width } = containerRef.current!.getBoundingClientRect()
      const keyIndex = Math.floor(((e.pageX - x) / width) * instrumentKeys.length)
      setActiveIndex(keyIndex)
    },
    [instrumentKeys.length]
  )

  return (
    <div
      ref={containerRef}
      className={classes.instrumentKeyRow}
      onPointerEnter={updateActiveIndex}
      onPointerDown={updateActiveIndex}
      onPointerMove={updateActiveIndex}
      onPointerLeave={() => setActiveIndex(-1)}
      onPointerUp={() => setActiveIndex(-1)}
    >
      {instrumentButtons}
    </div>
  )
}

const InstrumentButton: React.FC<InstrumentKey & { active: boolean; stage: StageViewModel; keyIndex: number }> = ({
  active,
  effectFn,
  keyIndex,
  songId,
  stage,
}) => {
  const classes = useStyles()
  const eventBus = useContext(EventBusContext)

  useEffect(() => {
    if (active) {
      const topLeft = { x: (stage.width / 8) * keyIndex, y: 0 }
      const bottomRight = { x: (stage.width / 8) * (keyIndex + 1), y: stage.height }

      eventBus.sendWebSocketCommand<LiveDataModifyCommand>({
        type: 'LDM',
        me: false,
        tp: [topLeft, bottomRight],
        d: 0,
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
  }, [active, effectFn, eventBus, keyIndex, songId, stage.height, stage.width])

  return (
    <div
      style={{ pointerEvents: 'none' }}
      className={`${classes.instrumentKey} ${active ? classes.activeInstrumentKey : ''}`}
    >
      <MusicNoteRounded />
    </div>
  )
}

export default StageLiveInstrumentPage
