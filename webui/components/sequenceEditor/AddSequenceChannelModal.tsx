'use client'

import { EditItemModal, EditItemSubmitHandler } from '@/components/dashboard/EditItemModal'
import { StagePropViewModel } from '@/src/types/viewModels'
import { FieldError, Input, Label, ListBox, Select, TextField } from '@heroui/react'
import { useEffect, useState } from 'react'

export type AddSequenceChannelModalProps = {
  isOpen: boolean
  onOpenChange: (isOpen: boolean) => void
  stageProps: StagePropViewModel[]
  onAdd: (stagePropId: string, name: string) => void
}

export function AddSequenceChannelModal({
  isOpen,
  onOpenChange,
  stageProps,
  onAdd,
}: AddSequenceChannelModalProps) {
  const [stagePropId, setStagePropId] = useState<string | null>(null)
  const [name, setName] = useState('')
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (isOpen) {
      setStagePropId(null)
      setName('')
      setError(null)
    }
  }, [isOpen])

  const handleSubmit: EditItemSubmitHandler = (event, close) => {
    event.preventDefault()

    if (!stagePropId) {
      setError('Please select a stage prop.')
      return
    }
    if (!name.trim()) {
      setError('Please enter a channel name.')
      return
    }

    onAdd(stagePropId, name.trim())
    close()
  }

  return (
    <EditItemModal
      error={error}
      formId='add-sequence-channel-form'
      isOpen={isOpen}
      submitLabel='Add channel'
      title='Add channel'
      onOpenChange={onOpenChange}
      onSubmit={handleSubmit}
    >
      <Select
        isRequired
        placeholder='Select a stage prop'
        value={stagePropId}
        onChange={selected => {
          const value = selected as string
          setStagePropId(value)

          const stageProp = stageProps.find(candidate => candidate.id === value)
          if (stageProp) {
            setName(stageProp.name)
          }
        }}
      >
        <Label>Stage prop</Label>
        <Select.Trigger>
          <Select.Value />
          <Select.Indicator />
        </Select.Trigger>
        <Select.Popover>
          <ListBox>
            {stageProps.map(stageProp => (
              <ListBox.Item key={stageProp.id} id={stageProp.id} textValue={stageProp.name}>
                {stageProp.name}
                <ListBox.ItemIndicator />
              </ListBox.Item>
            ))}
          </ListBox>
        </Select.Popover>
      </Select>

      <TextField isRequired name='name' value={name} onChange={setName}>
        <Label>Channel name</Label>
        <Input placeholder='Channel name' />
        <FieldError />
      </TextField>
    </EditItemModal>
  )
}
