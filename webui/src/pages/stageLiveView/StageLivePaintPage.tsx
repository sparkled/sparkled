import { makeStyles } from '@material-ui/styles'
import React, { useContext, useEffect } from 'react'
import { RouteComponentProps } from 'react-router-dom'
import PageContainer from '../../components/PageContainer'
import StageEditor from '../../components/stageEditor/StageEditor'
import useAxiosSwr from '../../hooks/api/useAxiosSwr.ts'
import { EventBusContext } from '../../hooks/useEventBus.ts'
import {
  LiveDataModifyCommand,
  LiveDataSubscribeCommand,
  LiveDataUnsubscribeCommand,
  ToggleInteractiveModeCommand,
} from '../../types/viewModels.ts'

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
}))

type Props = RouteComponentProps<{ stageId: string }>

const StageLivePaintPage: React.FC<Props> = props => {
  const classes = useStyles()
  const eventBus = useContext(EventBusContext)

  const { data } = useAxiosSwr({ url: `/stages/${props.match.params.stageId}` }, true)

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

    function getRandomColor() {
      const letters = '0123456789ABCDEF'
      let color = ''
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)]
      }
      return color
    }

    setInterval(() => {
      eventBus.sendWebSocketCommand<LiveDataModifyCommand>({
        type: 'LDM',
        m: [],
      })
    }, 50)

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
      <div className={classes.container}>{data && <StageEditor stage={data} />}</div>
    </PageContainer>
  )
}

export default StageLivePaintPage
