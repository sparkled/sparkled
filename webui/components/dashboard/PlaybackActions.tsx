'use client'

import { useApiAdjustPlayback } from '@/hooks/api/useApi'
import { Button } from '@heroui/react'
import { Play, Square } from 'lucide-react'

export type PlaybackActionsProps = {
  sequenceId?: string
  playlistId?: string
}

export function PlaybackActions({ sequenceId, playlistId }: PlaybackActionsProps) {
  const { trigger: adjustPlayback } = useApiAdjustPlayback()

  const handlePlay = () => {
    if (sequenceId) {
      adjustPlayback({ action: 'PLAY_SEQUENCE', sequenceId })
    } else if (playlistId) {
      adjustPlayback({ action: 'PLAY_PLAYLIST', playlistId })
    }
  }

  const handleStop = () => {
    adjustPlayback({ action: 'STOP' })
  }

  return (
    <div className='flex items-center gap-1'>
      <Button isIconOnly aria-label='Play' size='sm' variant='ghost' onPress={handlePlay}>
        <Play size={16} />
      </Button>
      <Button isIconOnly aria-label='Stop' size='sm' variant='ghost' onPress={handleStop}>
        <Square size={16} />
      </Button>
    </div>
  )
}
