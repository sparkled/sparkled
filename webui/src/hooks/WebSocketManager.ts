import React, { useCallback, useContext, useEffect, useLayoutEffect, useRef, useState } from 'react'
import useWebSocket from 'react-use-websocket'
import { useAppDispatch } from '../store/reducers/rootReducer.ts'
import { PingCommand, SparkledCommand } from '../types/viewModels.ts'
import { EventBusContext } from './useEventBus'

const pingFrequencyMs = 2000
const websocketTimeoutMs = 5000
const maxReconnectAttempts = 5

const WebSocketManager: React.FC = () => {
  const eventBus = useContext(EventBusContext)
  const webSocketRef = useServerWebSocket()

  useEffect(() => {
    eventBus.setWebSocket(webSocketRef)
  }, [eventBus, webSocketRef])

  return null
}

const useServerWebSocket = () => {
  const eventBus = useContext(EventBusContext)
  const dispatch = useAppDispatch()

  const [reconnectAttempts, setReconnectAttempts] = useState(0)
  const [lastResponseAt, setLastResponseAt] = useState(Date.now())
  const connected = useRef(true)
  const lastPingSentAt = useRef(0)

  const buildUrl = useCallback(async () => {
    // connectCount has no meaning in the URL, it's simply a trigger to force a websocket reconnect when no WebSocket
    // activity has been detected for a while.
    // TODO use .env file.
    const host = process.env.NODE_ENV === 'production' ? window.location.host : 'localhost:8080'
    return `ws://${host}/api/websocket?connectCount=${reconnectAttempts}`
  }, [reconnectAttempts])

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
        type: 'P',
        ts: new Date().toISOString(),
      })
      lastPingSentAt.current = now
    }, pingFrequencyMs)

    return () => clearInterval(interval)
  }, [eventBus])

  const { lastJsonMessage } = webSocket
  useEffect(() => {
    if (lastJsonMessage) {
      eventBus.dispatch('responseReceived', lastJsonMessage as SparkledCommand)

      if (!connected.current) {
        connected.current = true
      }

      setReconnectAttempts(0)
      setLastResponseAt(Date.now())
    }
  }, [dispatch, eventBus, lastJsonMessage])

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
  }, [lastResponseAt, webSocket])

  return webSocketRef
}

export { websocketTimeoutMs }
export default WebSocketManager
