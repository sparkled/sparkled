import React, {
  useCallback,
  useContext,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react'
import useWebSocket from 'react-use-websocket'
import useApi from '../../../api/useApi'
import { EventBusContext } from './useEventBus'
import { useAppDispatch } from '../../../redux/reduxHooks'
import { AssessmentCommand, PingCommand } from '../../../types/assessmentCommands'

const pingFrequencyMs = 1000
const websocketTimeoutMs = 5000
const maxReconnectAttempts = 5

type Props = {
  onReconnect: () => void
}

const WebSocketManager: React.FC<Props> = ({ onReconnect }) => {
  const eventBus = useContext(EventBusContext)

  const webSocketRef = useWebSocket(onReconnect)

  useEffect(() => {
    eventBus.setWebSocket(webSocketRef)
  }, [eventBus, webSocketRef])

  return null
}

const useServerWebSocket = (onReconnect: () => void) => {
  const eventBus = useContext(EventBusContext)
  const dispatch = useAppDispatch()
  const api = useApi()

  const [reconnectAttempts, setReconnectAttempts] = useState(0)
  const [lastResponseAt, setLastResponseAt] = useState(Date.now())
  const connected = useRef(true)
  const lastPingSentAt = useRef(0)

  const buildUrl = useCallback(async () => {
    // connectCount has no meaning in the URL, it's simply a trigger to force a websocket reconnect when no WebSocket
    // activity has been detected for a while.
    return `./v1.0/websocket?connectCount=${reconnectAttempts}`
  }, [api, reconnectAttempts])

  const webSocket = useWebSocket(buildUrl, {
    reconnectAttempts: 10,
    reconnectInterval: 500,
    shouldReconnect: () => true,
    retryOnError: true,
  })

  const webSocketRef = useRef<typeof webSocket>(webSocket)
  useLayoutEffect(() => {
    webSocketRef.current = webSocket
  }, [webSocket])

  // Send out regular pings.
  useEffect(() => {
    const interval = setInterval(() => {
      const now = Date.now()
      eventBus.sendWebSocketCommand<PingCommand>({
        t: 'p',
        ct: now,
      })
      lastPingSentAt.current = now
    }, pingFrequencyMs)

    return () => clearInterval(interval)
  }, [api, eventBus])

  // Mark person as active when a ping response is received.
  const { lastJsonMessage } = webSocket
  useEffect(() => {
    if (lastJsonMessage) {
      eventBus.dispatch('responseReceived', lastJsonMessage as AssessmentCommand)

      if (!connected.current) {
        connected.current = true
        onReconnect()
      }

      setReconnectAttempts(0)
      setLastResponseAt(Date.now())
    }
  }, [dispatch, eventBus, lastJsonMessage, onReconnect])

  // Monitor ping responses to detect connectivity issues.
  useEffect(() => {
    const timeout = setTimeout(() => {
      if (Date.now() - lastResponseAt > websocketTimeoutMs) {
        try {
          connected.current = false
          setReconnectAttempts(it => {
            const attempts = it + 1
            const message = `No response received from websocket after ${attempts} retry attempt(s), reopening.`

            const log = attempts === maxReconnectAttempts ? console.error : console.warn
            log(message)

            return attempts
          })
        } catch (e) {
          console.error('Failed to update reconnect attempts.', e)
        }
      }
    }, pingFrequencyMs)

    return () => clearTimeout(timeout)
  }, [
    api,
    lastResponseAt,
    webSocket,
  ])

  return webSocketRef
}

export { websocketTimeoutMs }
export default WebSocketManager
