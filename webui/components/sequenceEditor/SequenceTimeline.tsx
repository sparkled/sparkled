'use client'

import {
  CHANNEL_LABEL_WIDTH,
  SequenceChannelRow,
} from '@/components/sequenceEditor/SequenceChannelRow'
import { SequenceWaveform, SequenceWaveformHandle } from '@/components/sequenceEditor/SequenceWaveform'
import { SEQUENCE_FRAMES_PER_SECOND } from '@/src/constants/sequence'
import { Effect, SequenceViewModel } from '@/src/types/viewModels'
import { formatDuration } from '@/utils/format'
import { forwardRef, useImperativeHandle, useMemo, useRef } from 'react'

export type SequenceTimelineHandle = {
  playFrom: (frame: number) => void
  stop: () => void
}

export type SequenceTimelineProps = {
  sequence: SequenceViewModel
  pixelsPerFrame: number
  currentFrame: number
  playbackFrame: number | null
  selectedChannelId: string | null
  selectedEffectId: string | null
  onSeek: (frame: number) => void
  onSelectChannel: (channelId: string) => void
  onSelectEffect: (channelId: string, effectId: string | null) => void
  onChangeEffect: (channelId: string, effect: Effect) => void
  onPlaybackAudioProcess: (frame: number) => void
  onPlaybackEnd: () => void
}

const RULER_HEIGHT = 24

export const SequenceTimeline = forwardRef<SequenceTimelineHandle, SequenceTimelineProps>(
  function SequenceTimeline(
    {
      sequence,
      pixelsPerFrame,
      currentFrame,
      playbackFrame,
      selectedChannelId,
      selectedEffectId,
      onSeek,
      onSelectChannel,
      onSelectEffect,
      onChangeEffect,
      onPlaybackAudioProcess,
      onPlaybackEnd,
    },
    ref,
  ) {
    const waveformRef = useRef<SequenceWaveformHandle>(null)

    useImperativeHandle(ref, () => ({
      playFrom: frame => waveformRef.current?.playFrom(frame),
      stop: () => waveformRef.current?.stop(),
    }))

    const width = sequence.frameCount * pixelsPerFrame

    const secondMarks = useMemo(() => {
      const marks: { left: number; label: string }[] = []
      const totalSeconds = Math.ceil(sequence.frameCount / SEQUENCE_FRAMES_PER_SECOND)
      for (let second = 0; second <= totalSeconds; second++) {
        marks.push({
          left: second * SEQUENCE_FRAMES_PER_SECOND * pixelsPerFrame,
          label: formatDuration(second * 1000),
        })
      }
      return marks
    }, [sequence.frameCount, pixelsPerFrame])

    return (
      <div className='border-default h-full overflow-auto rounded-xl border'>
        <div className='relative' style={{ width: width + CHANNEL_LABEL_WIDTH }}>
          <div className='flex'>
            <div
              className='bg-surface border-default sticky left-0 z-20 shrink-0 border-r border-b'
              style={{ width: CHANNEL_LABEL_WIDTH, height: RULER_HEIGHT }}
            />
            <div
              className='bg-surface border-default relative border-b'
              style={{ width, height: RULER_HEIGHT }}
            >
              {secondMarks.map(mark => (
                <div
                  key={mark.left}
                  className='text-muted border-default absolute top-0 h-full border-l pl-1 text-[10px] leading-6'
                  style={{ left: mark.left }}
                >
                  {mark.label}
                </div>
              ))}
            </div>
          </div>

          <div className='flex flex-col'>
            {sequence.channels.map(channel => (
              <SequenceChannelRow
                key={channel.id}
                channel={channel}
                frameCount={sequence.frameCount}
                isSelected={channel.id === selectedChannelId}
                pixelsPerFrame={pixelsPerFrame}
                selectedEffectId={selectedEffectId}
                onChangeEffect={effect => onChangeEffect(channel.id, effect)}
                onSeek={onSeek}
                onSelectChannel={() => onSelectChannel(channel.id)}
                onSelectEffect={effectId => onSelectEffect(channel.id, effectId)}
              />
            ))}
          </div>

          <div className='flex'>
            <div
              className='bg-surface border-default sticky left-0 z-20 shrink-0 border-r'
              style={{ width: CHANNEL_LABEL_WIDTH, height: 48 }}
            />
            <SequenceWaveform
              ref={waveformRef}
              frameCount={sequence.frameCount}
              pixelsPerFrame={pixelsPerFrame}
              sequenceId={sequence.id}
              onAudioProcess={onPlaybackAudioProcess}
              onPlaybackEnd={onPlaybackEnd}
              onSeek={onSeek}
            />
          </div>

          <div
            className='bg-accent/70 pointer-events-none absolute top-0 bottom-0'
            style={{
              left: CHANNEL_LABEL_WIDTH + currentFrame * pixelsPerFrame,
              width: Math.max(1, pixelsPerFrame),
            }}
          />
          {playbackFrame !== null && (
            <div
              className='pointer-events-none absolute top-0 bottom-0 bg-red-500'
              style={{
                left: CHANNEL_LABEL_WIDTH + playbackFrame * pixelsPerFrame,
                width: Math.max(1, pixelsPerFrame),
              }}
            />
          )}
        </div>
      </div>
    )
  },
)
