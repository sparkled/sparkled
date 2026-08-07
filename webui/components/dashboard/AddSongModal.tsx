'use client'

import { AddItemModal, AddItemSubmitHandler } from '@/components/dashboard/AddItemModal'
import { useApiCreateSong, useApiGetDashboard } from '@/hooks/api/useApi'
import { FieldError, Input, Label, TextField } from '@heroui/react'
import { ChangeEvent, useState } from 'react'

function getAudioDurationMs(file: File): Promise<number> {
  return new Promise((resolve, reject) => {
    const audio = document.createElement('audio')
    const url = URL.createObjectURL(file)

    audio.addEventListener('loadedmetadata', () => {
      URL.revokeObjectURL(url)
      resolve(Math.round(audio.duration * 1000))
    })
    audio.addEventListener('error', () => {
      URL.revokeObjectURL(url)
      reject(new Error('Unable to read audio file.'))
    })

    audio.src = url
  })
}

export function AddSongModal() {
  const [name, setName] = useState('')
  const [artist, setArtist] = useState('')
  const [mp3, setMp3] = useState<File | null>(null)
  const [error, setError] = useState<string | null>(null)
  const { trigger: createSong, isMutating } = useApiCreateSong()
  const { mutate: mutateDashboard } = useApiGetDashboard()

  const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
    setMp3(event.target.files?.[0] ?? null)
  }

  const handleSubmit: AddItemSubmitHandler = async (event, close) => {
    event.preventDefault()
    setError(null)

    if (!mp3) {
      setError('Please select an MP3 file.')
      return
    }

    try {
      const durationMs = await getAudioDurationMs(mp3)

      await createSong({ mp3, song: { artist: artist || undefined, durationMs, name } })
      await mutateDashboard()
      setName('')
      setArtist('')
      setMp3(null)
      close()
    } catch {
      setError('Failed to add song. Please try again.')
    }
  }

  return (
    <AddItemModal
      ariaLabel='Add song'
      error={error}
      formId='add-song-form'
      isSubmitting={isMutating}
      submitLabel='Add song'
      title='Add song'
      onSubmit={handleSubmit}
    >
      <TextField isRequired name='name' value={name} onChange={setName}>
        <Label>Name</Label>
        <Input placeholder='Song name' />
        <FieldError />
      </TextField>

      <TextField name='artist' value={artist} onChange={setArtist}>
        <Label>Artist</Label>
        <Input placeholder='Artist (optional)' />
      </TextField>

      <div className='flex flex-col gap-1'>
        <label className='text-foreground text-sm font-medium' htmlFor='song-mp3'>
          MP3 file
        </label>
        <input
          accept='.mp3,audio/mpeg'
          className='text-muted text-sm'
          id='song-mp3'
          type='file'
          onChange={handleFileChange}
        />
      </div>
    </AddItemModal>
  )
}
