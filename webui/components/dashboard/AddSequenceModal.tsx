'use client'

import { AddItemModal, AddItemSubmitHandler } from '@/components/dashboard/AddItemModal'
import {
  useApiCreateSequence,
  useApiGetSequences,
  useApiGetSongs,
  useApiGetStages,
} from '@/hooks/api/useApi'
import { FieldError, Input, Label, ListBox, Select, TextField } from '@heroui/react'
import { useState } from 'react'

export function AddSequenceModal() {
  const [name, setName] = useState('')
  const [songId, setSongId] = useState<string | null>(null)
  const [stageId, setStageId] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)

  const { data: songs } = useApiGetSongs()
  const { data: stages } = useApiGetStages()
  const { trigger: createSequence, isMutating } = useApiCreateSequence()
  const { mutate: mutateSequences } = useApiGetSequences()

  const handleSubmit: AddItemSubmitHandler = async (event, close) => {
    event.preventDefault()
    setError(null)

    if (!songId || !stageId) {
      setError('Please select a song and a stage.')
      return
    }

    try {
      await createSequence({
        channels: [],
        frameCount: 0,
        name,
        songId,
        stageId,
        status: 'NEW',
      })
      await mutateSequences()
      setName('')
      setSongId(null)
      setStageId(null)
      close()
    } catch {
      setError('Failed to add sequence. Please try again.')
    }
  }

  return (
    <AddItemModal
      ariaLabel='Add sequence'
      error={error}
      formId='add-sequence-form'
      isSubmitting={isMutating}
      submitLabel='Add sequence'
      title='Add sequence'
      onSubmit={handleSubmit}
    >
      <TextField isRequired name='name' value={name} onChange={setName}>
        <Label>Name</Label>
        <Input placeholder='Sequence name' />
        <FieldError />
      </TextField>

      <Select
        isRequired
        placeholder='Select a song'
        value={songId}
        onChange={selected => setSongId(selected as string)}
      >
        <Label>Song</Label>
        <Select.Trigger>
          <Select.Value />
          <Select.Indicator />
        </Select.Trigger>
        <Select.Popover>
          <ListBox>
            {(songs ?? []).map(song => (
              <ListBox.Item key={song.id} id={song.id} textValue={song.name}>
                {song.name}
                <ListBox.ItemIndicator />
              </ListBox.Item>
            ))}
          </ListBox>
        </Select.Popover>
      </Select>

      <Select
        isRequired
        placeholder='Select a stage'
        value={stageId}
        onChange={selected => setStageId(selected as string)}
      >
        <Label>Stage</Label>
        <Select.Trigger>
          <Select.Value />
          <Select.Indicator />
        </Select.Trigger>
        <Select.Popover>
          <ListBox>
            {(stages ?? []).map(stage => (
              <ListBox.Item key={stage.id} id={stage.id} textValue={stage.name}>
                {stage.name}
                <ListBox.ItemIndicator />
              </ListBox.Item>
            ))}
          </ListBox>
        </Select.Popover>
      </Select>
    </AddItemModal>
  )
}
