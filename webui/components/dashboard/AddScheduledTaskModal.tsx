'use client'

import { AddItemModal, AddItemSubmitHandler } from '@/components/dashboard/AddItemModal'
import {
  useApiCreateScheduledTask,
  useApiGetDashboard,
  useApiGetPlaylists,
} from '@/hooks/api/useApi'
import { ScheduledActionType, ScheduledActionTypeValues } from '@/src/types/viewModels'
import { scheduledActionTypeLabel } from '@/utils/labels'
import { FieldError, Input, Label, ListBox, Select, TextField } from '@heroui/react'
import { useState } from 'react'

export function AddScheduledTaskModal() {
  const [cronExpression, setCronExpression] = useState('')
  const [type, setType] = useState<ScheduledActionType>('NONE')
  const [playlistId, setPlaylistId] = useState<string | null>(null)
  const [value, setValue] = useState('')
  const [error, setError] = useState<string | null>(null)

  const { data: playlists } = useApiGetPlaylists()
  const { trigger: createScheduledTask, isMutating } = useApiCreateScheduledTask()
  const { mutate: mutateDashboard } = useApiGetDashboard()

  const handleSubmit: AddItemSubmitHandler = async (event, close) => {
    event.preventDefault()
    setError(null)

    if (type === 'PLAY_PLAYLIST' && !playlistId) {
      setError('Please select a playlist.')
      return
    }

    try {
      await createScheduledTask({
        cronExpression,
        playlistId: type === 'PLAY_PLAYLIST' ? (playlistId ?? undefined) : undefined,
        type,
        value: type === 'SET_BRIGHTNESS' ? value : undefined,
      })
      await mutateDashboard()
      setCronExpression('')
      setType('NONE')
      setPlaylistId(null)
      setValue('')
      close()
    } catch {
      setError('Failed to add scheduled task. Please try again.')
    }
  }

  return (
    <AddItemModal
      ariaLabel='Add scheduled task'
      error={error}
      formId='add-scheduled-task-form'
      isSubmitting={isMutating}
      submitLabel='Add scheduled task'
      title='Add scheduled task'
      onSubmit={handleSubmit}
    >
      <TextField
        isRequired
        name='cronExpression'
        value={cronExpression}
        onChange={setCronExpression}
      >
        <Label>Cron expression</Label>
        <Input placeholder='0 0 * * * ?' />
        <FieldError />
      </TextField>

      <Select
        isRequired
        placeholder='Select an action'
        value={type}
        onChange={selected => setType(selected as ScheduledActionType)}
      >
        <Label>Action</Label>
        <Select.Trigger>
          <Select.Value />
          <Select.Indicator />
        </Select.Trigger>
        <Select.Popover>
          <ListBox>
            {ScheduledActionTypeValues.map(actionType => (
              <ListBox.Item
                key={actionType}
                id={actionType}
                textValue={scheduledActionTypeLabel[actionType]}
              >
                {scheduledActionTypeLabel[actionType]}
                <ListBox.ItemIndicator />
              </ListBox.Item>
            ))}
          </ListBox>
        </Select.Popover>
      </Select>

      {type === 'PLAY_PLAYLIST' && (
        <Select
          isRequired
          placeholder='Select a playlist'
          value={playlistId}
          onChange={selected => setPlaylistId(selected as string)}
        >
          <Label>Playlist</Label>
          <Select.Trigger>
            <Select.Value />
            <Select.Indicator />
          </Select.Trigger>
          <Select.Popover>
            <ListBox>
              {(playlists ?? []).map(playlist => (
                <ListBox.Item key={playlist.id} id={playlist.id} textValue={playlist.name}>
                  {playlist.name}
                  <ListBox.ItemIndicator />
                </ListBox.Item>
              ))}
            </ListBox>
          </Select.Popover>
        </Select>
      )}

      {type === 'SET_BRIGHTNESS' && (
        <TextField isRequired name='value' value={value} onChange={setValue}>
          <Label>Brightness (0-255)</Label>
          <Input placeholder='255' type='number' />
          <FieldError />
        </TextField>
      )}
    </AddItemModal>
  )
}
