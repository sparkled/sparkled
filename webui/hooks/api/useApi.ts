import {
  PlaylistActionViewModel,
  PlaylistEditViewModel,
  PlaylistSummaryViewModel,
  PlaylistViewModel,
  ReferenceDataViewModel,
  RenderResult,
  ScheduledActionEditViewModel,
  ScheduledActionViewModel,
  ScheduledTaskSummaryViewModel,
  SequenceEditViewModel,
  SequenceSummaryViewModel,
  SequenceViewModel,
  SettingEditViewModel,
  SettingViewModel,
  SongEditViewModel,
  SongViewModel,
  StageEditViewModel,
  StageSummaryViewModel,
  StageViewModel,
} from '@/src/types/viewModels'
import useSWR, { SWRConfiguration } from 'swr'
import useSWRMutation from 'swr/mutation'

const API_BASE_URL = process.env.NODE_ENV === 'production' ? '/api' : 'http://localhost:8080/api'

const jsonHeaders = { 'Content-Type': 'application/json' }

async function parseResponse<TResponse>(response: Response): Promise<TResponse> {
  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}: ${response.statusText}`)
  }

  if (response.status === 204) {
    return undefined as TResponse
  }

  return (await response.json()) as TResponse
}

async function fetchJson<TResponse>(url: string): Promise<TResponse> {
  const response = await fetch(`${API_BASE_URL}${url}`)

  return parseResponse<TResponse>(response)
}

async function sendJson<TResponse>(
  url: string,
  method: 'POST' | 'PUT' | 'DELETE',
  body?: unknown,
): Promise<TResponse> {
  const response = await fetch(`${API_BASE_URL}${url}`, {
    method,
    headers: body === undefined ? undefined : jsonHeaders,
    body: body === undefined ? undefined : JSON.stringify(body),
  })

  return parseResponse<TResponse>(response)
}

async function sendMultipart<TResponse>(url: string, formData: FormData): Promise<TResponse> {
  const response = await fetch(`${API_BASE_URL}${url}`, {
    method: 'POST',
    body: formData,
  })

  return parseResponse<TResponse>(response)
}

// Player

export function useApiAdjustPlayback() {
  return useSWRMutation<void, Error, string, PlaylistActionViewModel>('/player', (url, { arg }) =>
    sendJson<void>(url, 'POST', arg),
  )
}

// Playlists

export function useApiGetPlaylists(config?: SWRConfiguration) {
  return useSWR<PlaylistSummaryViewModel[]>('/playlists', fetchJson, config)
}

export function useApiGetPlaylist(id?: string, config?: SWRConfiguration) {
  return useSWR<PlaylistViewModel>(id ? `/playlists/${id}` : null, fetchJson, config)
}

export function useApiCreatePlaylist() {
  return useSWRMutation<PlaylistViewModel, Error, string, PlaylistEditViewModel>(
    '/playlists',
    (url, { arg }) => sendJson<PlaylistViewModel>(url, 'POST', arg),
  )
}

export type UpdatePlaylistArgs = {
  id: string
  playlist: PlaylistEditViewModel
}

export function useApiUpdatePlaylist() {
  return useSWRMutation<void, Error, string, UpdatePlaylistArgs>('/playlists', (_url, { arg }) =>
    sendJson<void>(`/playlists/${arg.id}`, 'PUT', arg.playlist),
  )
}

export function useApiDeletePlaylist() {
  return useSWRMutation<void, Error, string, string>('/playlists', (_url, { arg: id }) =>
    sendJson<void>(`/playlists/${id}`, 'DELETE'),
  )
}

// Reference data

export function useApiGetReferenceData(config?: SWRConfiguration) {
  return useSWR<ReferenceDataViewModel>('/reference-data', fetchJson, config)
}

// Scheduled tasks

export function useApiGetScheduledTasks(config?: SWRConfiguration) {
  return useSWR<ScheduledTaskSummaryViewModel[]>('/scheduledTasks', fetchJson, config)
}

export function useApiGetScheduledTask(id?: string, config?: SWRConfiguration) {
  return useSWR<ScheduledActionViewModel>(id ? `/scheduledTasks/${id}` : null, fetchJson, config)
}

export function useApiCreateScheduledTask() {
  return useSWRMutation<ScheduledActionViewModel, Error, string, ScheduledActionEditViewModel>(
    '/scheduledTasks',
    (url, { arg }) => sendJson<ScheduledActionViewModel>(url, 'POST', arg),
  )
}

export type UpdateScheduledTaskArgs = {
  id: string
  scheduledTask: ScheduledActionEditViewModel
}

export function useApiUpdateScheduledTask() {
  return useSWRMutation<ScheduledActionViewModel, Error, string, UpdateScheduledTaskArgs>(
    '/scheduledTasks',
    (_url, { arg }) =>
      sendJson<ScheduledActionViewModel>(`/scheduledTasks/${arg.id}`, 'PUT', arg.scheduledTask),
  )
}

export function useApiDeleteScheduledTask() {
  return useSWRMutation<void, Error, string, string>('/scheduledTasks', (_url, { arg: id }) =>
    sendJson<void>(`/scheduledTasks/${id}`, 'DELETE'),
  )
}

// Sequences

export function useApiGetSequences(config?: SWRConfiguration) {
  return useSWR<SequenceSummaryViewModel[]>('/sequences', fetchJson, config)
}

export function useApiGetSequence(id?: string, config?: SWRConfiguration) {
  return useSWR<SequenceViewModel>(id ? `/sequences/${id}` : null, fetchJson, config)
}

export function useApiGetSequenceStage(id?: string, config?: SWRConfiguration) {
  return useSWR<StageViewModel>(id ? `/sequences/${id}/stage` : null, fetchJson, config)
}

export function getSequenceSongAudioUrl(id: string) {
  return `${API_BASE_URL}/sequences/${id}/songAudio`
}

export function useApiCreateSequence() {
  return useSWRMutation<SequenceViewModel, Error, string, SequenceEditViewModel>(
    '/sequences',
    (url, { arg }) => sendJson<SequenceViewModel>(url, 'POST', arg),
  )
}

export type UpdateSequenceArgs = {
  id: string
  sequence: SequenceEditViewModel
}

export function useApiUpdateSequence() {
  return useSWRMutation<void, Error, string, UpdateSequenceArgs>('/sequences', (_url, { arg }) =>
    sendJson<void>(`/sequences/${arg.id}`, 'PUT', arg.sequence),
  )
}

export type PreviewSequenceArgs = {
  id: string
  sequence: SequenceEditViewModel
  startFrame?: number
  frameCount?: number
}

export function useApiPreviewSequence() {
  return useSWRMutation<RenderResult, Error, string, PreviewSequenceArgs>(
    '/sequences/preview',
    (_url, { arg }) => {
      const query = new URLSearchParams()

      if (arg.startFrame !== undefined) {
        query.set('startFrame', String(arg.startFrame))
      }
      if (arg.frameCount !== undefined) {
        query.set('frameCount', String(arg.frameCount))
      }

      const queryString = query.toString() ? `?${query.toString()}` : ''

      return sendJson<RenderResult>(
        `/sequences/${arg.id}/preview${queryString}`,
        'POST',
        arg.sequence,
      )
    },
  )
}

export function useApiDeleteSequence() {
  return useSWRMutation<void, Error, string, string>('/sequences', (_url, { arg: id }) =>
    sendJson<void>(`/sequences/${id}`, 'DELETE'),
  )
}

// Settings

export function useApiGetSetting(id?: string, config?: SWRConfiguration) {
  return useSWR<SettingViewModel>(id ? `/settings/${id}` : null, fetchJson, config)
}

export type UpdateSettingArgs = {
  id: string
  setting: SettingEditViewModel
}

export function useApiUpdateSetting() {
  return useSWRMutation<SettingViewModel, Error, string, UpdateSettingArgs>(
    '/settings',
    (_url, { arg }) => sendJson<SettingViewModel>(`/settings/${arg.id}`, 'PUT', arg.setting),
  )
}

// Songs

export function useApiGetSongs(config?: SWRConfiguration) {
  return useSWR<SongViewModel[]>('/songs', fetchJson, config)
}

export function useApiGetSong(id?: string, config?: SWRConfiguration) {
  return useSWR<SongViewModel>(id ? `/songs/${id}` : null, fetchJson, config)
}

export type CreateSongArgs = {
  song: SongEditViewModel
  mp3: File
}

export function useApiCreateSong() {
  return useSWRMutation<SongViewModel, Error, string, CreateSongArgs>('/songs', (url, { arg }) => {
    const formData = new FormData()

    formData.append('song', JSON.stringify(arg.song))
    formData.append('mp3', arg.mp3)

    return sendMultipart<SongViewModel>(url, formData)
  })
}

export function useApiDeleteSong() {
  return useSWRMutation<void, Error, string, string>('/songs', (_url, { arg: id }) =>
    sendJson<void>(`/songs/${id}`, 'DELETE'),
  )
}

// Stages

export function useApiGetStages(config?: SWRConfiguration) {
  return useSWR<StageSummaryViewModel[]>('/stages', fetchJson, config)
}

export function useApiGetStage(id?: string, config?: SWRConfiguration) {
  return useSWR<StageViewModel>(id ? `/stages/${id}` : null, fetchJson, config)
}

export function useApiCreateStage() {
  return useSWRMutation<StageViewModel, Error, string, StageEditViewModel>(
    '/stages',
    (url, { arg }) => sendJson<StageViewModel>(url, 'POST', arg),
  )
}

export type UpdateStageArgs = {
  id: string
  stage: StageEditViewModel
}

export function useApiUpdateStage() {
  return useSWRMutation<StageViewModel, Error, string, UpdateStageArgs>(
    '/stages',
    (_url, { arg }) => sendJson<StageViewModel>(`/stages/${arg.id}`, 'PUT', arg.stage),
  )
}

export function useApiDeleteStage() {
  return useSWRMutation<void, Error, string, string>('/stages', (_url, { arg: id }) =>
    sendJson<void>(`/stages/${id}`, 'DELETE'),
  )
}
