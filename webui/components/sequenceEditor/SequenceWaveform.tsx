'use client'

import { getSequenceSongAudioUrl } from '@/hooks/api/useApi'
import WaveSurfer from 'wavesurfer.js'
import { forwardRef, useEffect, useImperativeHandle, useRef } from 'react'

export type SequenceWaveformHandle = {
  playFrom: (frame: number) => void
  stop: () => void
}

export type SequenceWaveformProps = {
  sequenceId: string
  frameCount: number
  framesPerSecond: number
  pixelsPerFrame: number
  onSeek: (frame: number) => void
  onAudioProcess: (frame: number) => void
  onPlaybackEnd: () => void
}

/** Renders the sequence's song audio as a waveform using wavesurfer.js, and drives playback for previews. */
export const SequenceWaveform = forwardRef<SequenceWaveformHandle, SequenceWaveformProps>(
  function SequenceWaveform(
    { sequenceId, frameCount, framesPerSecond, pixelsPerFrame, onSeek, onAudioProcess, onPlaybackEnd },
    ref,
  ) {
    const containerRef = useRef<HTMLDivElement>(null)
    const waveSurferRef = useRef<WaveSurfer | null>(null)
    const callbacksRef = useRef({ onSeek, onAudioProcess, onPlaybackEnd, frameCount, framesPerSecond })
    callbacksRef.current = { onSeek, onAudioProcess, onPlaybackEnd, frameCount, framesPerSecond }

    useEffect(() => {
      const container = containerRef.current
      if (!container) {
        return
      }

      const waveSurfer = WaveSurfer.create({
        container,
        height: 48,
        waveColor: '#a1a1aa',
        progressColor: '#3b82f6',
        cursorWidth: 0,
        interact: true,
        url: getSequenceSongAudioUrl(sequenceId),
      })

      waveSurfer.on('interaction', time => {
        const { frameCount, framesPerSecond } = callbacksRef.current
        const frame = Math.min(frameCount - 1, Math.max(0, Math.round(time * framesPerSecond)))
        callbacksRef.current.onSeek(frame)
      })

      waveSurfer.on('audioprocess', time => {
        callbacksRef.current.onAudioProcess(Math.round(time * callbacksRef.current.framesPerSecond))
      })

      waveSurfer.on('finish', () => callbacksRef.current.onPlaybackEnd())

      waveSurferRef.current = waveSurfer

      return () => {
        waveSurfer.destroy()
        waveSurferRef.current = null
      }
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [sequenceId])

    useImperativeHandle(ref, () => ({
      playFrom(frame: number) {
        const waveSurfer = waveSurferRef.current
        if (!waveSurfer) {
          return
        }

        waveSurfer.setTime(frame / callbacksRef.current.framesPerSecond)
        void waveSurfer.play()
      },
      stop() {
        waveSurferRef.current?.stop()
      },
    }))

    const width = frameCount * pixelsPerFrame

    return (
      <div style={{ height: 48, width }}>
        <div ref={containerRef} />
      </div>
    )
  },
)
