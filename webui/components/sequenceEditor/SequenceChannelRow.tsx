'use client'

import { EffectBlock } from '@/components/sequenceEditor/EffectBlock'
import { Effect, SequenceChannelViewModel } from '@/src/types/viewModels'
import clsx from 'clsx'
import { MouseEvent } from 'react'

export type SequenceChannelRowProps = {
  channel: SequenceChannelViewModel
  frameCount: number
  pixelsPerFrame: number
  isSelected: boolean
  selectedEffectId: string | null
  onSelectChannel: () => void
  onSeek: (frame: number) => void
  onSelectEffect: (effectId: string | null) => void
  onChangeEffect: (effect: Effect) => void
}

export const CHANNEL_ROW_HEIGHT = 40
export const CHANNEL_LABEL_WIDTH = 128

export function SequenceChannelRow({
  channel,
  frameCount,
  pixelsPerFrame,
  isSelected,
  selectedEffectId,
  onSelectChannel,
  onSeek,
  onSelectEffect,
  onChangeEffect,
}: SequenceChannelRowProps) {
  const width = frameCount * pixelsPerFrame

  function handleLaneClick(event: MouseEvent<HTMLDivElement>) {
    onSelectChannel()
    onSelectEffect(null)

    const rect = event.currentTarget.getBoundingClientRect()
    const frame = Math.round((event.clientX - rect.left) / pixelsPerFrame)
    onSeek(Math.min(Math.max(frame, 0), frameCount - 1))
  }

  function handleLabelClick() {
    onSelectChannel()
    onSelectEffect(null)
  }

  return (
    <div className='flex'>
      <div
        className={clsx(
          'bg-surface border-default sticky left-0 z-10 flex shrink-0 cursor-pointer items-center border-r border-b px-2 text-xs font-medium',
          isSelected ? 'text-accent' : 'text-muted',
        )}
        style={{ width: CHANNEL_LABEL_WIDTH, height: CHANNEL_ROW_HEIGHT }}
        onClick={handleLabelClick}
      >
        <span className='truncate'>{channel.name}</span>
      </div>
      <div
        className={clsx('border-default relative border-b', isSelected && 'bg-accent/5')}
        style={{ width, height: CHANNEL_ROW_HEIGHT }}
        onClick={handleLaneClick}
      >
        {channel.effects.map(effect => (
          <EffectBlock
            key={effect.id}
            effect={effect}
            frameCount={frameCount}
            isSelected={effect.id === selectedEffectId}
            pixelsPerFrame={pixelsPerFrame}
            onChange={(startFrame, endFrame) => onChangeEffect({ ...effect, startFrame, endFrame })}
            onSelect={() => {
              onSelectChannel()
              onSelectEffect(effect.id)
            }}
          />
        ))}
      </div>
    </div>
  )
}
