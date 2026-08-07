'use client'

import { AddItemModal, AddItemSubmitHandler } from '@/components/dashboard/AddItemModal'
import { useApiCreatePlaylist, useApiGetPlaylists } from '@/hooks/api/useApi'
import { FieldError, Input, Label, TextField } from '@heroui/react'
import { useState } from 'react'

export function AddPlaylistModal() {
  const [name, setName] = useState('')
  const [error, setError] = useState<string | null>(null)
  const { trigger: createPlaylist, isMutating } = useApiCreatePlaylist()
  const { mutate: mutatePlaylists } = useApiGetPlaylists()

  const handleSubmit: AddItemSubmitHandler = async (event, close) => {
    event.preventDefault()
    setError(null)

    try {
      await createPlaylist({ name, insertions: [], deletions: [] })
      await mutatePlaylists()
      setName('')
      close()
    } catch {
      setError('Failed to add playlist. Please try again.')
    }
  }

  return (
    <AddItemModal
      ariaLabel='Add playlist'
      error={error}
      formId='add-playlist-form'
      isSubmitting={isMutating}
      submitLabel='Add playlist'
      title='Add playlist'
      onSubmit={handleSubmit}
    >
      <TextField isRequired name='name' value={name} onChange={setName}>
        <Label>Name</Label>
        <Input placeholder='Playlist name' />
        <FieldError />
      </TextField>
    </AddItemModal>
  )
}
