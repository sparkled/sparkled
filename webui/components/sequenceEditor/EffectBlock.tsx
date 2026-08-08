'use client'

import { Effect } from '@/src/types/viewModels'
import clsx from 'clsx'
import { PointerEvent, useEffect, useRef, useState } from 'react'

export type EffectBlockProps = {
  effect: Effect
  pixelsPerFrame: number
  frameCount: number
  isSelected: boolean
  onSelect: () => void
  onChange: (startFrame: number, endFrame: number) => void
}

type DragKind = 'move' | 'resize-start' | 'resize-end'

type DragState = {
  kind: DragKind
  pointerStartX: number
  startFrame: number
  endFrame: number
}

const MIN_DURATION_FRAMES = 1
const DRAG_THRESHOLD_PX = 3

/**
 * A single effect rendered as a draggable, resizable block within a sequence channel's lane. Dragging the
 * body moves the effect; dragging either edge resizes it. Clicking without dragging selects it.
 */
export function EffectBlock({
  effect,
  pixelsPerFrame,
  frameCount,
  isSelected,
  onSelect,
  onChange,
}: EffectBlockProps) {
  const [preview, setPreview] = useState<{ startFrame: number; endFrame: number } | null>(null)
  const dragStateRef = useRef<DragState | null>(null)
  const movedRef = useRef(false)
  const previewRef = useRef(preview)
  previewRef.current = preview
  const paramsRef = useRef({ pixelsPerFrame, frameCount, onSelect, onChange })
  paramsRef.current = { pixelsPerFrame, frameCount, onSelect, onChange }

  useEffect(() => {
    function handlePointerMove(event: globalThis.PointerEvent) {
      const dragState = dragStateRef.current
      if (!dragState) {
        return
      }

      const { pixelsPerFrame, frameCount } = paramsRef.current
      const deltaPx = event.clientX - dragState.pointerStartX
      if (Math.abs(deltaPx) > DRAG_THRESHOLD_PX) {
        movedRef.current = true
      }
      const deltaFrames = Math.round(deltaPx / pixelsPerFrame)

      let nextStart = dragState.startFrame
      let nextEnd = dragState.endFrame

      if (dragState.kind === 'move') {
        const duration = dragState.endFrame - dragState.startFrame
        nextStart = clamp(dragState.startFrame + deltaFrames, 0, frameCount - 1 - duration)
        nextEnd = nextStart + duration
      } else if (dragState.kind === 'resize-start') {
        nextStart = clamp(dragState.startFrame + deltaFrames, 0, dragState.endFrame - MIN_DURATION_FRAMES)
      } else {
        nextEnd = clamp(
          dragState.endFrame + deltaFrames,
          dragState.startFrame + MIN_DURATION_FRAMES,
          frameCount - 1,
        )
      }

      setPreview({ startFrame: nextStart, endFrame: nextEnd })
    }

    function handlePointerUp() {
      const dragState = dragStateRef.current
      dragStateRef.current = null

      if (dragState) {
        if (movedRef.current && previewRef.current) {
          paramsRef.current.onChange(previewRef.current.startFrame, previewRef.current.endFrame)
        } else {
          paramsRef.current.onSelect()
        }
      }

      setPreview(null)
      movedRef.current = false
    }

    window.addEventListener('pointermove', handlePointerMove)
    window.addEventListener('pointerup', handlePointerUp)
    return () => {
      window.removeEventListener('pointermove', handlePointerMove)
      window.removeEventListener('pointerup', handlePointerUp)
    }
  }, [])

  function startDrag(kind: DragKind, event: PointerEvent) {
    event.stopPropagation()
    movedRef.current = false
    dragStateRef.current = {
      kind,
      pointerStartX: event.clientX,
      startFrame: effect.startFrame,
      endFrame: effect.endFrame,
    }
  }

  const displayed = preview ?? { startFrame: effect.startFrame, endFrame: effect.endFrame }
  const left = displayed.startFrame * pixelsPerFrame
  const width = (displayed.endFrame - displayed.startFrame + 1) * pixelsPerFrame

  return (
    <div
      className={clsx(
        'absolute top-1 bottom-1 flex cursor-grab items-center overflow-hidden rounded-md border px-2 text-xs font-medium select-none',
        isSelected
          ? 'bg-accent/30 border-accent text-foreground'
          : 'bg-surface border-default text-muted hover:text-foreground',
      )}
      style={{ left, width }}
      onClick={event => event.stopPropagation()}
      onPointerDown={event => startDrag('move', event)}
    >
      <div
        className='absolute top-0 left-0 h-full w-1.5 cursor-ew-resize'
        onPointerDown={event => startDrag('resize-start', event)}
      />
      <span className='truncate'>{effect.type}</span>
      <div
        className='absolute top-0 right-0 h-full w-1.5 cursor-ew-resize'
        onPointerDown={event => startDrag('resize-end', event)}
      />
    </div>
  )
}

function clamp(value: number, min: number, max: number) {
  return Math.min(Math.max(value, min), max)
}
