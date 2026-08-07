'use client'

import { CronBuilder } from '@/components/dashboard/CronBuilder'
import {
  PlaylistSummaryViewModel,
  ScheduledActionType,
  ScheduledActionTypeValues,
} from '@/src/types/viewModels'
import { scheduledActionTypeLabel } from '@/utils/labels'
import { FieldError, Input, Label, ListBox, Select, TextField } from '@heroui/react'

export type ScheduledTaskFormFieldsProps = {
  cronBuilderKey: number
  cronExpression: string
  onCronExpressionChange: (value: string) => void
  type: ScheduledActionType
  onTypeChange: (type: ScheduledActionType) => void
  playlistId: string | null
  onPlaylistIdChange: (playlistId: string) => void
  playlists: PlaylistSummaryViewModel[]
  value: string
  onValueChange: (value: string) => void
}

export function ScheduledTaskFormFields({
  cronBuilderKey,
  cronExpression,
  onCronExpressionChange,
  type,
  onTypeChange,
  playlistId,
  onPlaylistIdChange,
  playlists,
  value,
  onValueChange,
}: ScheduledTaskFormFieldsProps) {
  return (
    <>
      <CronBuilder key={cronBuilderKey} value={cronExpression} onChange={onCronExpressionChange} />

      <Select
        isRequired
        placeholder='Select an action'
        value={type}
        onChange={selected => onTypeChange(selected as ScheduledActionType)}
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
          onChange={selected => onPlaylistIdChange(selected as string)}
        >
          <Label>Playlist</Label>
          <Select.Trigger>
            <Select.Value />
            <Select.Indicator />
          </Select.Trigger>
          <Select.Popover>
            <ListBox>
              {playlists.map(playlist => (
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
        <TextField isRequired name='value' value={value} onChange={onValueChange}>
          <Label>Brightness (0-255)</Label>
          <Input placeholder='255' type='number' />
          <FieldError />
        </TextField>
      )}
    </>
  )
}
