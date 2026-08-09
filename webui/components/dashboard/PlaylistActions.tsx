'use client'

import { EditPlaylistModal } from '@/components/dashboard/EditPlaylistModal'
import { Button, useOverlayState } from '@heroui/react'
import { Pencil } from 'lucide-react'

export type PlaylistActionsProps = {
  playlistId: string
}

export function PlaylistActions({ playlistId }: PlaylistActionsProps) {
  const editState = useOverlayState()

  return (
    <>
      <Button isIconOnly aria-label='Edit playlist' size='sm' variant='ghost' onPress={editState.open}>
        <Pencil size={16} />
      </Button>
      <EditPlaylistModal
        isOpen={editState.isOpen}
        playlistId={playlistId}
        onOpenChange={editState.setOpen}
      />
    </>
  )
}
