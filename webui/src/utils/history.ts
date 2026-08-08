import { useCallback, useState } from 'react'

type History<T> = {
  past: T[]
  present: T
  future: T[]
}

export type HistoryState<T> = {
  value: T
  canUndo: boolean
  canRedo: boolean
  set: (updater: T | ((previous: T) => T)) => void
  reset: (value: T) => void
  undo: () => void
  redo: () => void
}

/**
 * A minimal undo/redo history hook, similar in spirit to `redux-undo` but implemented as a plain React hook
 * so the sequence editor doesn't need to pull in Redux just to support undo/redo.
 */
export function useHistoryState<T>(initialValue: T): HistoryState<T> {
  const [history, setHistory] = useState<History<T>>({
    past: [],
    present: initialValue,
    future: [],
  })

  const set = useCallback((updater: T | ((previous: T) => T)) => {
    setHistory(previous => {
      const nextPresent =
        typeof updater === 'function' ? (updater as (previous: T) => T)(previous.present) : updater

      if (nextPresent === previous.present) {
        return previous
      }

      return { past: [...previous.past, previous.present], present: nextPresent, future: [] }
    })
  }, [])

  const reset = useCallback((value: T) => {
    setHistory({ past: [], present: value, future: [] })
  }, [])

  const undo = useCallback(() => {
    setHistory(previous => {
      if (previous.past.length === 0) {
        return previous
      }

      const newPresent = previous.past[previous.past.length - 1]
      return {
        past: previous.past.slice(0, -1),
        present: newPresent,
        future: [previous.present, ...previous.future],
      }
    })
  }, [])

  const redo = useCallback(() => {
    setHistory(previous => {
      if (previous.future.length === 0) {
        return previous
      }

      const [newPresent, ...remainingFuture] = previous.future
      return {
        past: [...previous.past, previous.present],
        present: newPresent,
        future: remainingFuture,
      }
    })
  }, [])

  return {
    value: history.present,
    canUndo: history.past.length > 0,
    canRedo: history.future.length > 0,
    set,
    reset,
    undo,
    redo,
  }
}
