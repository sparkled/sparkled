'use client'

import { AddSequenceChannelModal } from '@/components/sequenceEditor/AddSequenceChannelModal'
import { EffectPropertiesPanel } from '@/components/sequenceEditor/EffectPropertiesPanel'
import { SequenceStagePreview } from '@/components/sequenceEditor/SequenceStagePreview'
import {
  SequenceTimeline,
  SequenceTimelineHandle,
} from '@/components/sequenceEditor/SequenceTimeline'
import {
  useApiGetReferenceData,
  useApiGetSequence,
  useApiGetSequenceStage,
  useApiGetSong,
  useApiPreviewSequence,
  useApiUpdateSequence,
} from '@/hooks/api/useApi'
import { SEQUENCE_FRAMES_PER_SECOND } from '@/src/constants/sequence'
import {
  Effect,
  SequenceChannelViewModel,
  SequenceEditViewModel,
  SequenceStatus,
  SequenceViewModel,
} from '@/src/types/viewModels'
import { useHistoryState } from '@/src/utils/history'
import { generateId } from '@/src/utils/id'
import {
  DecodedPreviewRender,
  LedColor,
  PreviewRenderResult,
  decodePreviewRender,
  getStagePropFrameColors,
} from '@/src/utils/renderData'
import { formatDuration } from '@/utils/format'
import { sequenceStatusColor } from '@/utils/labels'
import { Alert, Button, Chip, Input, ListBox, Select, Skeleton, TextField } from '@heroui/react'
import { Layers, Play, Redo, Rocket, Save, Square, Star, Undo } from 'lucide-react'
import NextLink from 'next/link'
import { useEffect, useMemo, useRef, useState } from 'react'

const NEW_EFFECT_FRAME_COUNT = 10
const MIN_PIXELS_PER_FRAME = 1
const MAX_PIXELS_PER_FRAME = 8
const DEFAULT_PIXELS_PER_FRAME = 3
const PREVIEW_DURATION_SECONDS_VALUES = [2, 5, 10, 20, 30, 60] as const

export type SequenceEditorProps = {
  sequenceId: string
}

export function SequenceEditor({ sequenceId }: SequenceEditorProps) {
  const {
    data: sequence,
    error: sequenceError,
    isLoading: sequenceLoading,
    mutate: mutateSequence,
  } = useApiGetSequence(sequenceId)
  const {
    data: stage,
    error: stageError,
    isLoading: stageLoading,
  } = useApiGetSequenceStage(sequenceId)
  const { data: referenceData } = useApiGetReferenceData()
  const { data: song } = useApiGetSong(sequence?.songId)

  const { trigger: updateSequence, isMutating: isSaving } = useApiUpdateSequence()
  const { trigger: previewSequence, isMutating: isFetchingPreview } = useApiPreviewSequence()

  const history = useHistoryState<SequenceViewModel | null>(null)
  const editedSequence = history.value

  const [selectedChannelId, setSelectedChannelId] = useState<string | null>(null)
  const [selectedEffectId, setSelectedEffectId] = useState<string | null>(null)
  const [currentFrame, setCurrentFrame] = useState(0)
  const [pixelsPerFrame, setPixelsPerFrame] = useState(DEFAULT_PIXELS_PER_FRAME)
  const [previewDuration, setPreviewDuration] = useState(5)
  const [saveError, setSaveError] = useState<string | null>(null)
  const [savedRecently, setSavedRecently] = useState(false)
  const [isAddChannelModalOpen, setAddChannelModalOpen] = useState(false)
  const [previewRender, setPreviewRender] = useState<DecodedPreviewRender | null>(null)
  const [playbackFrame, setPlaybackFrame] = useState<number | null>(null)

  const timelineRef = useRef<SequenceTimelineHandle>(null)

  useEffect(() => {
    if (sequence && !editedSequence) {
      history.reset(sequence)
      setSelectedChannelId(sequence.channels[0]?.id ?? null)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sequence])

  useEffect(() => {
    if (!savedRecently) {
      return
    }
    const timeout = setTimeout(() => setSavedRecently(false), 3000)
    return () => clearTimeout(timeout)
  }, [savedRecently])

  useEffect(() => {
    function handleKeyDown(event: KeyboardEvent) {
      const isMod = event.metaKey || event.ctrlKey
      if (!isMod || event.key.toLowerCase() !== 'z') {
        return
      }

      event.preventDefault()
      if (event.shiftKey) {
        history.redo()
      } else {
        history.undo()
      }
    }

    window.addEventListener('keydown', handleKeyDown)
    return () => window.removeEventListener('keydown', handleKeyDown)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  function updateSequenceState(updater: (sequence: SequenceViewModel) => SequenceViewModel) {
    history.set(previous => (previous ? updater(previous) : previous))
  }

  function handleSeek(frame: number) {
    if (!editedSequence) {
      return
    }
    setCurrentFrame(Math.min(Math.max(frame, 0), editedSequence.frameCount - 1))
  }

  function handleAddChannel(stagePropId: string, name: string) {
    if (!editedSequence) {
      return
    }

    const channel: SequenceChannelViewModel = {
      id: generateId(),
      displayOrder: editedSequence.channels.length,
      effects: [],
      name,
      stagePropId,
    }

    updateSequenceState(current => ({ ...current, channels: [...current.channels, channel] }))
    setSelectedChannelId(channel.id)
  }

  function handleAddEffect() {
    if (!editedSequence || !selectedChannelId || !referenceData) {
      return
    }

    const endFrame = Math.min(
      currentFrame + NEW_EFFECT_FRAME_COUNT - 1,
      editedSequence.frameCount - 1,
    )
    const effect: Effect = {
      id: generateId(),
      type: referenceData.effects[0]?.code ?? '',
      args: {},
      easing: { type: referenceData.easings[0]?.code ?? '', start: 0, end: 100, args: {} },
      fill: { blendMode: 'NORMAL', type: referenceData.fills[0]?.code ?? '', args: {} },
      startFrame: currentFrame,
      endFrame: Math.max(endFrame, currentFrame),
      repetitions: 1,
      repetitionSpacing: 0,
    }

    updateSequenceState(current => ({
      ...current,
      channels: current.channels.map(channel =>
        channel.id === selectedChannelId
          ? { ...channel, effects: [...channel.effects, effect].sort(byStartFrame) }
          : channel,
      ),
    }))
    setSelectedEffectId(effect.id)
  }

  function handleChangeEffect(channelId: string, updatedEffect: Effect) {
    updateSequenceState(current => ({
      ...current,
      channels: current.channels.map(channel =>
        channel.id === channelId
          ? {
              ...channel,
              effects: channel.effects
                .map(effect => (effect.id === updatedEffect.id ? updatedEffect : effect))
                .sort(byStartFrame),
            }
          : channel,
      ),
    }))
  }

  function handleUpdateSelectedEffect(changes: Partial<Effect>) {
    if (!selectedChannelId || !selectedEffect) {
      return
    }
    handleChangeEffect(selectedChannelId, { ...selectedEffect, ...changes })
  }

  function handleDeleteSelectedEffect() {
    if (!selectedChannelId || !selectedEffectId) {
      return
    }

    updateSequenceState(current => ({
      ...current,
      channels: current.channels.map(channel =>
        channel.id === selectedChannelId
          ? {
              ...channel,
              effects: channel.effects.filter(effect => effect.id !== selectedEffectId),
            }
          : channel,
      ),
    }))
    setSelectedEffectId(null)
  }

  function handleSelectChannel(channelId: string) {
    setSelectedChannelId(channelId)
  }

  function handleSelectEffect(channelId: string, effectId: string | null) {
    setSelectedChannelId(channelId)
    setSelectedEffectId(effectId)
  }

  function toEditViewModel(
    current: SequenceViewModel,
    status: SequenceStatus,
  ): SequenceEditViewModel {
    return {
      channels: current.channels,
      frameCount: current.frameCount,
      name: current.name,
      songId: current.songId,
      stageId: current.stageId,
      status,
    }
  }

  async function persist(status: SequenceStatus) {
    if (!editedSequence) {
      return
    }

    setSaveError(null)

    try {
      await updateSequence({ id: sequenceId, sequence: toEditViewModel(editedSequence, status) })
      history.set(current => (current ? { ...current, status } : current))
      await mutateSequence()
      setSavedRecently(true)
    } catch (e) {
      console.error(e)
      setSaveError(
        `Failed to ${status === 'PUBLISHED' ? 'publish' : 'save'} sequence. Please try again.`,
      )
    }
  }

  function handleCancelPreview() {
    setPreviewRender(null)
    setPlaybackFrame(null)
    timelineRef.current?.stop()
  }

  async function handlePreview() {
    if (!editedSequence) {
      return
    }

    setSaveError(null)

    const frameCount = Math.min(
      Math.round(SEQUENCE_FRAMES_PER_SECOND * previewDuration),
      editedSequence.frameCount - currentFrame,
    )

    if (frameCount <= 0) {
      return
    }

    try {
      const result = await previewSequence({
        id: sequenceId,
        sequence: toEditViewModel(editedSequence, editedSequence.status),
        startFrame: currentFrame,
        frameCount,
      })

      setPreviewRender(decodePreviewRender(result))
      setPlaybackFrame(currentFrame)
      timelineRef.current?.playFrom(currentFrame)
    } catch (e) {
      console.error(e)
      setSaveError('Failed to render preview. Please try again.')
    }
  }

  function handlePlaybackAudioProcess(frame: number) {
    setPlaybackFrame(current => (current === null ? current : frame))
  }

  const selectedEffect = useMemo(() => {
    if (!editedSequence || !selectedChannelId || !selectedEffectId) {
      return null
    }
    const channel = editedSequence.channels.find(candidate => candidate.id === selectedChannelId)
    return channel?.effects.find(effect => effect.id === selectedEffectId) ?? null
  }, [editedSequence, selectedChannelId, selectedEffectId])

  const ledColorsByStagePropCode = useMemo(() => {
    if (!previewRender || playbackFrame === null || !editedSequence || !stage) {
      return undefined
    }

    const stagePropCodesById = new Map(
      stage.stageProps.map(stageProp => [stageProp.id, stageProp.code]),
    )
    const relativeFrame = playbackFrame - previewRender.startFrame
    const colors: Record<string, LedColor[]> = {}
    editedSequence.channels.forEach(channel => {
      const stagePropCode = stagePropCodesById.get(channel.stagePropId)
      if (stagePropCode) {
        colors[stagePropCode] = getStagePropFrameColors(previewRender, stagePropCode, relativeFrame)
      }
    })
    return colors
  }, [previewRender, playbackFrame, editedSequence, stage])

  const loaded = Boolean(editedSequence && stage)
  const isPreviewing = previewRender !== null

  return (
    <section className='flex h-full flex-col gap-4 py-8'>
      <div className='flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center'>
        <div className='flex flex-col gap-1'>
          <NextLink className='text-muted hover:text-foreground text-xs' href='/sequences'>
            ← Back to sequences
          </NextLink>
          {editedSequence ? (
            <>
              <div className='flex items-center gap-2'>
                <TextField
                  aria-label='Sequence name'
                  className='min-w-[200px]'
                  value={editedSequence.name}
                  onChange={name => updateSequenceState(current => ({ ...current, name }))}
                >
                  <Input className='text-xl font-semibold' placeholder='Sequence name' />
                </TextField>
                <Chip color={sequenceStatusColor[editedSequence.status]} size='sm'>
                  {editedSequence.status}
                </Chip>
              </div>
              {song && (
                <p className='text-muted text-xs'>
                  {song.name} · {formatDuration(song.durationMs)}
                </p>
              )}
            </>
          ) : (
            <Skeleton className='h-8 w-48 rounded-lg' />
          )}
        </div>

        <div className='flex flex-wrap items-center gap-2'>
          {savedRecently && <span className='text-success text-sm'>Saved</span>}

          <Select
            aria-label='Preview duration'
            className='w-24'
            value={String(previewDuration)}
            onChange={selected => setPreviewDuration(Number(selected))}
          >
            <Select.Trigger>
              <Select.Value />
              <Select.Indicator />
            </Select.Trigger>
            <Select.Popover>
              <ListBox>
                {PREVIEW_DURATION_SECONDS_VALUES.map(seconds => (
                  <ListBox.Item key={seconds} id={String(seconds)} textValue={`${seconds}s`}>
                    {seconds}s
                    <ListBox.ItemIndicator />
                  </ListBox.Item>
                ))}
              </ListBox>
            </Select.Popover>
          </Select>

          <Button
            isIconOnly
            aria-label='Undo'
            isDisabled={!loaded || !history.canUndo}
            size='sm'
            variant='ghost'
            onPress={() => history.undo()}
          >
            <Undo size={16} />
          </Button>

          <Button
            isIconOnly
            aria-label='Redo'
            isDisabled={!loaded || !history.canRedo}
            size='sm'
            variant='ghost'
            onPress={() => history.redo()}
          >
            <Redo size={16} />
          </Button>

          <Button
            isIconOnly
            aria-label='Add channel'
            isDisabled={!loaded}
            size='sm'
            variant='ghost'
            onPress={() => setAddChannelModalOpen(true)}
          >
            <Layers size={16} />
          </Button>

          <Button
            isIconOnly
            aria-label='Add effect'
            isDisabled={!loaded || !selectedChannelId || !referenceData}
            size='sm'
            variant='ghost'
            onPress={handleAddEffect}
          >
            <Star size={16} />
          </Button>

          <Button
            isDisabled={!loaded || isSaving}
            size='sm'
            variant='secondary'
            onPress={() => persist('DRAFT')}
          >
            <Save size={16} />
            {isSaving ? 'Saving...' : 'Save'}
          </Button>

          {isPreviewing ? (
            <Button size='sm' variant='secondary' onPress={handleCancelPreview}>
              <Square size={16} />
              Stop
            </Button>
          ) : (
            <Button
              isDisabled={!loaded || isFetchingPreview}
              size='sm'
              variant='secondary'
              onPress={handlePreview}
            >
              <Play size={16} />
              {isFetchingPreview ? 'Rendering...' : 'Preview'}
            </Button>
          )}

          <Button isDisabled={!loaded || isSaving} size='sm' onPress={() => persist('PUBLISHED')}>
            <Rocket size={16} />
            Publish
          </Button>
        </div>
      </div>

      {(sequenceError || stageError) && (
        <Alert status='danger'>
          <Alert.Indicator />
          <Alert.Content>
            <Alert.Title>Unable to load sequence</Alert.Title>
            <Alert.Description>
              Something went wrong while loading this sequence. Please try again later.
            </Alert.Description>
          </Alert.Content>
        </Alert>
      )}

      {saveError && (
        <Alert status='danger'>
          <Alert.Indicator />
          <Alert.Content>
            <Alert.Title>Something went wrong</Alert.Title>
            <Alert.Description>{saveError}</Alert.Description>
          </Alert.Content>
        </Alert>
      )}

      {(sequenceLoading || stageLoading) && !loaded && (
        <div className='flex flex-col gap-3'>
          <Skeleton className='h-10 w-full rounded-lg' />
          <Skeleton className='h-64 w-full rounded-lg' />
        </div>
      )}

      {editedSequence && stage && (
        <div className='flex min-h-0 flex-1 flex-col gap-4'>
          <div className='flex min-h-0 flex-1 gap-0'>
            <div className='min-w-0 flex-1'>
              <SequenceStagePreview
                ledColorsByStagePropCode={ledColorsByStagePropCode}
                stage={stage}
              />
            </div>
            <EffectPropertiesPanel
              effect={selectedEffect}
              maxFrame={editedSequence.frameCount - 1}
              referenceData={referenceData}
              onChange={handleUpdateSelectedEffect}
              onDelete={handleDeleteSelectedEffect}
            />
          </div>

          <div className='flex shrink-0 items-center justify-end gap-2'>
            <Button
              isIconOnly
              aria-label='Zoom out'
              isDisabled={pixelsPerFrame <= MIN_PIXELS_PER_FRAME}
              size='sm'
              variant='ghost'
              onPress={() => setPixelsPerFrame(zoom => Math.max(MIN_PIXELS_PER_FRAME, zoom - 1))}
            >
              -
            </Button>
            <Button
              isIconOnly
              aria-label='Zoom in'
              isDisabled={pixelsPerFrame >= MAX_PIXELS_PER_FRAME}
              size='sm'
              variant='ghost'
              onPress={() => setPixelsPerFrame(zoom => Math.min(MAX_PIXELS_PER_FRAME, zoom + 1))}
            >
              +
            </Button>
          </div>

          <div className='h-72 shrink-0'>
            <SequenceTimeline
              ref={timelineRef}
              currentFrame={currentFrame}
              pixelsPerFrame={pixelsPerFrame}
              playbackFrame={playbackFrame}
              selectedChannelId={selectedChannelId}
              selectedEffectId={selectedEffectId}
              sequence={editedSequence}
              onChangeEffect={handleChangeEffect}
              onPlaybackAudioProcess={handlePlaybackAudioProcess}
              onPlaybackEnd={handleCancelPreview}
              onSeek={handleSeek}
              onSelectChannel={handleSelectChannel}
              onSelectEffect={handleSelectEffect}
            />
          </div>
        </div>
      )}

      {stage && (
        <AddSequenceChannelModal
          isOpen={isAddChannelModalOpen}
          stageProps={stage.stageProps}
          onAdd={handleAddChannel}
          onOpenChange={setAddChannelModalOpen}
        />
      )}
    </section>
  )
}

function byStartFrame(a: Effect, b: Effect) {
  return a.startFrame - b.startFrame
}
