'use client'

import { useApiGetPlaylists, useApiUpdatePlaylist } from '@/hooks/api/useApi'
import { Button, Dropdown } from '@heroui/react'
import { EllipsisVertical, ListMusic } from 'lucide-react'
import { Key } from 'react'

export type AddToPlaylistMenuProps = {
  sequenceId: string
}

export function AddToPlaylistMenu({ sequenceId }: AddToPlaylistMenuProps) {
  const { data: playlists, mutate: mutatePlaylists } = useApiGetPlaylists()
  const { trigger: updatePlaylist } = useApiUpdatePlaylist()

  const handleAddToPlaylist = async (playlistId: Key) => {
    const playlist = playlists?.find(candidate => candidate.id === playlistId)

    if (!playlist) {
      return
    }

    try {
      await updatePlaylist({
        id: playlist.id,
        playlist: {
          name: playlist.name,
          insertions: [{ sequenceId, displayOrder: playlist.sequenceCount }],
          deletions: [],
        },
      })
      await mutatePlaylists()
    } catch {
      // Ignore. The playlist will simply not be updated.
    }
  }

  return (
    <Dropdown>
      <Dropdown.Trigger>
        <Button isIconOnly aria-label='More actions' size='sm' variant='ghost'>
          <EllipsisVertical size={16} />
        </Button>
      </Dropdown.Trigger>
      <Dropdown.Popover placement='bottom end'>
        <Dropdown.Menu>
          <Dropdown.SubmenuTrigger>
            <Dropdown.Item textValue='Add to playlist'>
              <ListMusic size={16} />
              Add to playlist
              <Dropdown.SubmenuIndicator />
            </Dropdown.Item>
            <Dropdown.Popover>
              <Dropdown.Menu onAction={handleAddToPlaylist}>
                {(playlists ?? []).map(playlist => (
                  <Dropdown.Item key={playlist.id} id={playlist.id} textValue={playlist.name}>
                    {playlist.name}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown.Popover>
          </Dropdown.SubmenuTrigger>
        </Dropdown.Menu>
      </Dropdown.Popover>
    </Dropdown>
  )
}
