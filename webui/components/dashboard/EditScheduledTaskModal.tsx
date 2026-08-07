'use client'

import { EditItemModal, EditItemSubmitHandler } from '@/components/dashboard/EditItemModal'
import { ScheduledTaskFormFields } from '@/components/dashboard/ScheduledTaskFormFields'
import {
  useApiGetPlaylists,
  useApiGetScheduledTask,
  useApiGetScheduledTasks,
  useApiUpdateScheduledTask,
} from '@/hooks/api/useApi'
import { ScheduledActionType } from '@/src/types/viewModels'
import { Skeleton } from '@heroui/react'
import { useEffect, useState } from 'react'

export type EditScheduledTaskModalProps = {
  taskId: string
  isOpen: boolean
  onOpenChange: (isOpen: boolean) => void
}

export function EditScheduledTaskModal({
  taskId,
  isOpen,
  onOpenChange,
}: EditScheduledTaskModalProps) {
  const [cronExpression, setCronExpression] = useState('')
  const [cronBuilderKey, setCronBuilderKey] = useState(0)
  const [type, setType] = useState<ScheduledActionType>('NONE')
  const [playlistId, setPlaylistId] = useState<string | null>(null)
  const [value, setValue] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [syncedTaskId, setSyncedTaskId] = useState<string | null>(null)

  const { data: playlists } = useApiGetPlaylists()
  const {
    data: task,
    isLoading: isTaskLoading,
    mutate: mutateTask,
  } = useApiGetScheduledTask(isOpen ? taskId : undefined)
  const { trigger: updateScheduledTask, isMutating } = useApiUpdateScheduledTask()
  const { mutate: mutateScheduledTasks } = useApiGetScheduledTasks()

  useEffect(() => {
    if (task) {
      setCronExpression(task.cronExpression)
      setCronBuilderKey(key => key + 1)
      setType(task.type)
      setPlaylistId(task.playlistId ?? null)
      setValue(task.value ?? '')
      setError(null)
      setSyncedTaskId(task.id)
    }
  }, [task])

  const handleSubmit: EditItemSubmitHandler = async (event, close) => {
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
      const updatedTask = await updateScheduledTask({
        id: taskId,
        scheduledTask: {
          cronExpression,
          playlistId: type === 'PLAY_PLAYLIST' ? (playlistId ?? undefined) : undefined,
          type,
          value: type === 'SET_BRIGHTNESS' ? value : undefined,
        },
      })
      await mutateTask(updatedTask)
      await mutateScheduledTasks()
      close()
    } catch {
      setError('Failed to update scheduled task. Please try again.')
    }
  }

  return (
    <EditItemModal
      dialogClassName='sm:max-w-xl'
      error={error}
      formId='edit-scheduled-task-form'
      isOpen={isOpen}
      isSubmitting={isMutating}
      submitLabel='Save changes'
      title='Edit scheduled task'
      onOpenChange={onOpenChange}
      onSubmit={handleSubmit}
    >
      {isTaskLoading || !task || syncedTaskId !== task.id ? (
        <div className='flex flex-col gap-4'>
          <Skeleton className='h-10 w-full rounded-lg' />
          <Skeleton className='h-10 w-full rounded-lg' />
          <Skeleton className='h-10 w-full rounded-lg' />
        </div>
      ) : (
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
      )}
    </EditItemModal>
  )
}
