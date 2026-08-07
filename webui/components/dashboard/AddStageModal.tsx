'use client'

import { AddItemModal, AddItemSubmitHandler } from '@/components/dashboard/AddItemModal'
import { useApiCreateStage, useApiGetDashboard } from '@/hooks/api/useApi'
import { FieldError, Input, Label, TextField } from '@heroui/react'
import { useState } from 'react'

const DEFAULT_WIDTH = 800
const DEFAULT_HEIGHT = 600

export function AddStageModal() {
  const [name, setName] = useState('')
  const [error, setError] = useState<string | null>(null)
  const { trigger: createStage, isMutating } = useApiCreateStage()
  const { mutate: mutateDashboard } = useApiGetDashboard()

  const handleSubmit: AddItemSubmitHandler = async (event, close) => {
    event.preventDefault()
    setError(null)

    try {
      await createStage({ height: DEFAULT_HEIGHT, name, stageProps: [], width: DEFAULT_WIDTH })
      await mutateDashboard()
      setName('')
      close()
    } catch {
      setError('Failed to add stage. Please try again.')
    }
  }

  return (
    <AddItemModal
      ariaLabel='Add stage'
      error={error}
      formId='add-stage-form'
      isSubmitting={isMutating}
      submitLabel='Add stage'
      title='Add stage'
      onSubmit={handleSubmit}
    >
      <TextField isRequired name='name' value={name} onChange={setName}>
        <Label>Name</Label>
        <Input placeholder='Stage name' />
        <FieldError />
      </TextField>
    </AddItemModal>
  )
}
