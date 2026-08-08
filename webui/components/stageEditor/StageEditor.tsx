'use client'

import { StagePropCanvas } from '@/components/stageEditor/StagePropCanvas'
import { StagePropChanges, StagePropPanel } from '@/components/stageEditor/StagePropPanel'
import { useApiGetStage, useApiUpdateStage } from '@/hooks/api/useApi'
import {
  StageEditViewModel,
  StagePropType,
  StagePropViewModel,
  StageViewModel,
} from '@/src/types/viewModels'
import { generateId } from '@/src/utils/id'
import { computeLedPositions } from '@/src/utils/stagePropGeometry'
import { stagePropTypeLabel } from '@/utils/labels'
import { Alert, Button, Input, Skeleton, TextField } from '@heroui/react'
import NextLink from 'next/link'
import { useEffect, useState } from 'react'

const DEFAULT_LED_COUNT = 10
const DEFAULT_PROP_SIZE = 100

export type StageEditorProps = {
  stageId: string
}

export function StageEditor({ stageId }: StageEditorProps) {
  const { data: stage, error: loadError, isLoading, mutate } = useApiGetStage(stageId)
  const { trigger: updateStage, isMutating: isSaving } = useApiUpdateStage()

  const [editedStage, setEditedStage] = useState<StageViewModel | null>(null)
  const [selectedStagePropId, setSelectedStagePropId] = useState<string | null>(null)
  const [saveError, setSaveError] = useState<string | null>(null)
  const [savedRecently, setSavedRecently] = useState(false)

  useEffect(() => {
    if (stage && !editedStage) {
      setEditedStage(stage)
    }
  }, [stage, editedStage])

  useEffect(() => {
    if (!savedRecently) {
      return
    }
    const timeout = setTimeout(() => setSavedRecently(false), 3000)
    return () => clearTimeout(timeout)
  }, [savedRecently])

  function updateStageProps(updater: (stageProps: StagePropViewModel[]) => StagePropViewModel[]) {
    setEditedStage(prev => (prev ? { ...prev, stageProps: updater(prev.stageProps) } : prev))
  }

  function handleChangeStageProp(id: string, changes: StagePropChanges) {
    updateStageProps(stageProps =>
      stageProps.map(stageProp => (stageProp.id === id ? { ...stageProp, ...changes } : stageProp)),
    )
  }

  function handleMoveStageProp(id: string, positionX: number, positionY: number) {
    handleChangeStageProp(id, { positionX, positionY })
  }

  function handleRotateStageProp(id: string, rotation: number) {
    handleChangeStageProp(id, { rotation })
  }

  function handleResizeStageProp(
    id: string,
    positionX: number,
    positionY: number,
    scaleX: number,
    scaleY: number,
  ) {
    handleChangeStageProp(id, { positionX, positionY, scaleX, scaleY })
  }

  function handleAddStageProp(type: StagePropType) {
    if (!editedStage) {
      return
    }

    const displayOrder = editedStage.stageProps.length
    const newStageProp: StagePropViewModel = {
      id: generateId(),
      stageId: editedStage.id,
      code: `${type}_${displayOrder + 1}`,
      name: `${stagePropTypeLabel[type]} ${displayOrder + 1}`,
      type,
      ledCount: DEFAULT_LED_COUNT,
      ledOffset: 0,
      reverse: false,
      positionX: Math.round(editedStage.width / 2 - DEFAULT_PROP_SIZE / 2),
      positionY: Math.round(editedStage.height / 2 - DEFAULT_PROP_SIZE / 2),
      scaleX: 1,
      scaleY: 1,
      rotation: 0,
      brightness: 255,
      displayOrder,
      ledPositions: { points: [], bounds: { x1: 0, y1: 0, x2: 0, y2: 0 } },
    }

    updateStageProps(stageProps => [...stageProps, newStageProp])
    setSelectedStagePropId(newStageProp.id)
  }

  function handleDeleteStageProp(id: string) {
    updateStageProps(stageProps => stageProps.filter(stageProp => stageProp.id !== id))
    setSelectedStagePropId(prev => (prev === id ? null : prev))
  }

  function handleRenameStage(name: string) {
    setEditedStage(prev => (prev ? { ...prev, name } : prev))
  }

  async function handleSave() {
    if (!editedStage) {
      return
    }

    setSaveError(null)

    try {
      const payload: StageEditViewModel = {
        name: editedStage.name,
        width: editedStage.width,
        height: editedStage.height,
        stageProps: editedStage.stageProps.map(stageProp => ({
          ...stageProp,
          ledPositions: computeLedPositions(stageProp),
        })),
      }

      const updated = await updateStage({ id: stageId, stage: payload })
      setEditedStage(updated)
      await mutate(updated, { revalidate: false })
      setSavedRecently(true)
    } catch {
      setSaveError('Failed to save stage. Please try again.')
    }
  }

  const selectedStageProp =
    editedStage?.stageProps.find(stageProp => stageProp.id === selectedStagePropId) ?? null

  return (
    <section className='flex h-full flex-col gap-4 py-8'>
      <div className='flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center'>
        <div className='flex flex-col gap-1'>
          <NextLink className='text-muted hover:text-foreground text-xs' href='/stages'>
            ← Back to stages
          </NextLink>
          {editedStage ? (
            <TextField
              aria-label='Stage name'
              className='min-w-[200px]'
              value={editedStage.name}
              onChange={handleRenameStage}
            >
              <Input className='text-xl font-semibold' placeholder='Stage name' />
            </TextField>
          ) : (
            <Skeleton className='h-8 w-48 rounded-lg' />
          )}
        </div>

        <div className='flex items-center gap-3'>
          {savedRecently && <span className='text-success text-sm'>Saved</span>}
          <Button isDisabled={!editedStage || isSaving} onPress={handleSave}>
            {isSaving ? 'Saving...' : 'Save'}
          </Button>
        </div>
      </div>

      {loadError && (
        <Alert status='danger'>
          <Alert.Indicator />
          <Alert.Content>
            <Alert.Title>Unable to load stage</Alert.Title>
            <Alert.Description>
              Something went wrong while loading this stage. Please try again later.
            </Alert.Description>
          </Alert.Content>
        </Alert>
      )}

      {saveError && (
        <Alert status='danger'>
          <Alert.Indicator />
          <Alert.Content>
            <Alert.Title>Unable to save stage</Alert.Title>
            <Alert.Description>{saveError}</Alert.Description>
          </Alert.Content>
        </Alert>
      )}

      {isLoading && !editedStage && (
        <div className='flex flex-col gap-3'>
          <Skeleton className='h-10 w-full rounded-lg' />
          <Skeleton className='h-64 w-full rounded-lg' />
        </div>
      )}

      {editedStage && (
        <div className='bg-surface-secondary relative min-h-0 flex-1 overflow-hidden rounded-xl'>
          <StagePropCanvas
            stage={editedStage}
            selectedStagePropId={selectedStagePropId}
            onMoveStageProp={handleMoveStageProp}
            onResizeStageProp={handleResizeStageProp}
            onRotateStageProp={handleRotateStageProp}
            onSelectStageProp={setSelectedStagePropId}
          />
          <StagePropPanel
            stageProp={selectedStageProp}
            onAdd={handleAddStageProp}
            onChange={handleChangeStageProp}
            onDelete={handleDeleteStageProp}
          />
        </div>
      )}
    </section>
  )
}
