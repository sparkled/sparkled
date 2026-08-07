'use client'

import { AddItemModal, AddItemSubmitHandler } from '@/components/dashboard/AddItemModal'
import { ScheduledTaskFormFields } from '@/components/dashboard/ScheduledTaskFormFields'
import {
  useApiCreateScheduledTask,
  useApiGetPlaylists,
  useApiGetScheduledTasks,
} from '@/hooks/api/useApi'
import { ScheduledActionType } from '@/src/types/viewModels'
import { useState } from 'react'

export function AddScheduledTaskModal() {
  const [cronExpression, setCronExpression] = useState('')
  const [cronBuilderKey, setCronBuilderKey] = useState(0)
  const [type, setType] = useState<ScheduledActionType>('NONE')
  const [playlistId, setPlaylistId] = useState<string | null>(null)
  const [value, setValue] = useState('')
  const [error, setError] = useState<string | null>(null)

  const { data: playlists } = useApiGetPlaylists()
  const { trigger: createScheduledTask, isMutating } = useApiCreateScheduledTask()
  const { mutate: mutateScheduledTasks } = useApiGetScheduledTasks()

  const handleSubmit: AddItemSubmitHandler = async (event, close) => {
    event.preventDefault()
    setError(null)

    if (!cronExpression) {
      setError('Please choose a schedule.')
      return
    }

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
      await mutateScheduledTasks()
      setCronExpression('')
      setCronBuilderKey(key => key + 1)
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
      dialogClassName='sm:max-w-xl'
      error={error}
      formId='add-scheduled-task-form'
      isSubmitting={isMutating}
      submitLabel='Add scheduled task'
      title='Add scheduled task'
      onSubmit={handleSubmit}
    >
      <ScheduledTaskFormFields
        cronBuilderKey={cronBuilderKey}
        cronExpression={cronExpression}
        playlistId={playlistId}
        playlists={playlists ?? []}
        type={type}
        value={value}
        onCronExpressionChange={setCronExpression}
        onPlaylistIdChange={setPlaylistId}
        onTypeChange={setType}
        onValueChange={setValue}
      />
    </AddItemModal>
  )
}
