'use client'

import { EditItemModal, EditItemSubmitHandler } from '@/components/dashboard/EditItemModal'
import {
  useApiGetPlaylist,
  useApiGetPlaylists,
  useApiGetSequences,
  useApiUpdatePlaylist,
} from '@/hooks/api/useApi'
import { Button, FieldError, Input, Label, ListBox, Select, Skeleton, TextField } from '@heroui/react'
import { GripVertical, Plus, Trash2 } from 'lucide-react'
import { DragEvent, useEffect, useRef, useState } from 'react'

export type EditPlaylistModalProps = {
  playlistId: string
  isOpen: boolean
  onOpenChange: (isOpen: boolean) => void
}

type PlaylistRow = {
  key: string
  sequenceId: string
}

export function EditPlaylistModal({ playlistId, isOpen, onOpenChange }: EditPlaylistModalProps) {
  const [name, setName] = useState('')
  const [rows, setRows] = useState<PlaylistRow[]>([])
  const [dragKey, setDragKey] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [syncedPlaylistId, setSyncedPlaylistId] = useState<string | null>(null)
  const newRowCounter = useRef(0)

  const {
    data: playlist,
    isLoading: isPlaylistLoading,
    mutate: mutatePlaylist,
  } = useApiGetPlaylist(isOpen ? playlistId : undefined)
  const { data: sequences } = useApiGetSequences()
  const { trigger: updatePlaylist, isMutating } = useApiUpdatePlaylist()
  const { mutate: mutatePlaylists } = useApiGetPlaylists()

  useEffect(() => {
    if (playlist) {
      setName(playlist.name)
      setRows(
        [...playlist.sequences]
          .sort((a, b) => a.displayOrder - b.displayOrder)
          .map(sequence => ({ key: sequence.id, sequenceId: sequence.sequenceId })),
      )
      setError(null)
      setSyncedPlaylistId(playlist.id)
    }
  }, [playlist])

  const handleAddRow = () => {
    newRowCounter.current += 1
    setRows(current => [...current, { key: `new-${newRowCounter.current}`, sequenceId: '' }])
  }

  const handleRemoveRow = (key: string) => {
    setRows(current => current.filter(row => row.key !== key))
  }

  const handleSequenceChange = (key: string, sequenceId: string) => {
    setRows(current => current.map(row => (row.key === key ? { ...row, sequenceId } : row)))
  }

  const handleDragOver = (event: DragEvent<HTMLDivElement>, overKey: string) => {
    event.preventDefault()

    if (!dragKey || dragKey === overKey) {
      return
    }

    setRows(current => {
      const fromIndex = current.findIndex(row => row.key === dragKey)
      const toIndex = current.findIndex(row => row.key === overKey)
      if (fromIndex === -1 || toIndex === -1) {
        return current
      }

      const next = [...current]
      const [moved] = next.splice(fromIndex, 1)
      next.splice(toIndex, 0, moved)
      return next
    })
  }

  const handleSubmit: EditItemSubmitHandler = async (event, close) => {
    event.preventDefault()
    setError(null)

    if (!playlist) {
      return
    }

    const orderedSequenceIds = rows
      .map(row => row.sequenceId)
      .filter((sequenceId): sequenceId is string => sequenceId !== '')

    try {
      await updatePlaylist({
        id: playlist.id,
        playlist: {
          name,
          deletions: playlist.sequences.map(sequence => sequence.id),
          insertions: orderedSequenceIds.map((sequenceId, index) => ({
            sequenceId,
            displayOrder: index,
          })),
        },
      })
      await mutatePlaylist()
      await mutatePlaylists()
      close()
    } catch {
      setError('Failed to update playlist. Please try again.')
    }
  }

  const isLoading = isPlaylistLoading || !playlist || syncedPlaylistId !== playlist.id

  return (
    <EditItemModal
      dialogClassName='sm:max-w-xl'
      error={error}
      formId='edit-playlist-form'
      isOpen={isOpen}
      isSubmitting={isMutating}
      submitLabel='Save changes'
      title='Edit playlist'
      onOpenChange={onOpenChange}
      onSubmit={handleSubmit}
    >
      {isLoading ? (
        <div className='flex flex-col gap-4'>
          <Skeleton className='h-10 w-full rounded-lg' />
          <Skeleton className='h-10 w-full rounded-lg' />
          <Skeleton className='h-10 w-full rounded-lg' />
        </div>
      ) : (
        <>
          <TextField isRequired name='name' value={name} onChange={setName}>
            <Label>Name</Label>
            <Input placeholder='Playlist name' />
            <FieldError />
          </TextField>

          <div className='flex flex-col gap-2'>
            <Label>Sequences</Label>

            {rows.length === 0 && <p className='text-muted text-sm'>No sequences yet.</p>}

            {rows.map(row => (
              <div
                key={row.key}
                className='border-default flex items-center gap-2 rounded-lg border p-2'
                draggable
                onDragEnd={() => setDragKey(null)}
                onDragOver={event => handleDragOver(event, row.key)}
                onDragStart={() => setDragKey(row.key)}
              >
                <GripVertical className='text-muted shrink-0 cursor-grab' size={16} />
                <Select
                  aria-label='Sequence'
                  className='flex-1'
                  placeholder='Select a sequence'
                  value={row.sequenceId}
                  onChange={selected => handleSequenceChange(row.key, selected as string)}
                >
                  <Select.Trigger>
                    <Select.Value />
                    <Select.Indicator />
                  </Select.Trigger>
                  <Select.Popover>
                    <ListBox>
                      {(sequences ?? []).map(sequence => (
                        <ListBox.Item key={sequence.id} id={sequence.id} textValue={sequence.name}>
                          {sequence.name}
                          <ListBox.ItemIndicator />
                        </ListBox.Item>
                      ))}
                    </ListBox>
                  </Select.Popover>
                </Select>
                <Button
                  isIconOnly
                  aria-label='Remove sequence'
                  size='sm'
                  variant='ghost'
                  onPress={() => handleRemoveRow(row.key)}
                >
                  <Trash2 size={14} />
                </Button>
              </div>
            ))}

            <Button size='sm' variant='secondary' onPress={handleAddRow}>
              <Plus size={16} />
              Add sequence
            </Button>
          </div>
        </>
      )}
    </EditItemModal>
  )
}
