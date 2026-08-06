import { createContext, RefObject, useMemo } from 'react'
import { WebSocketHook } from 'react-use-websocket/dist/lib/types'
import { SparkledCommand } from '../types/viewModels.ts'

export const EventBusContext = createContext<EventBus>(null!)

export interface EventBus {
  addListener: (eventType: EventType, callback: EventCallback) => number
  removeListener: (listenerId: number) => void
  dispatch: (eventType: EventType, command: SparkledCommand) => void
  setWebSocket: (webSocket: RefObject<WebSocketHook>) => void
  sendWebSocketCommand: <T extends SparkledCommand>(command: T) => void
}

type EventType = 'commandSent' | 'responseReceived'
type EventCallback = (command: SparkledCommand) => void
type EventListenerEntry = {
  eventType: EventType
  listener: (command: SparkledCommand) => void
}

/**
 * Implements an efficient event bus mechanism for dispatching WebSocket commands and subscribing to them.
 */
function useEventBus() {
  const eventBus: EventBus = useMemo(() => {
    let webSocket: RefObject<WebSocketHook> | null = null
    const listeners: Record<number, EventListenerEntry> = {}

    let currentListenerId = 0

    const dispatch = (eventType: EventType, command: SparkledCommand) => {
      Object.values(listeners).forEach(entry => {
        if (entry.eventType === eventType) {
          entry.listener(command)
        }
      })
    }

    return {
      addListener(eventType, listener) {
        listeners[++currentListenerId] = { eventType, listener }
        return currentListenerId
      },

      removeListener(listenerId) {
        delete listeners[listenerId]
      },

      dispatch,

      setWebSocket(ws) {
        webSocket = ws
      },

      /**
       * Allow certain commands to be immediately processed by the client. This is an optimistic strategy that assumes
       * the command has been sent and processed correctly by the server.
       *
       * Only latency-sensitive commands should be processed locally. That is, commands that will mess up test results
       * if latency is introduced (e.g. clicking Trail Making Task items).
       */
      sendWebSocketCommand(command) {
        if (webSocket?.current == null) {
          console.error(`Failed to send command ${JSON.stringify(command)}, webSocket is null.`)
        } else {
          try {
            webSocket.current.sendJsonMessage(command)
            dispatch('commandSent', command)
          } catch (e) {
            console.error(`Failed to send command ${JSON.stringify(command)}.`, e)
          }
        }
      },
    }
  }, [])

  return eventBus
}

export default useEventBus
