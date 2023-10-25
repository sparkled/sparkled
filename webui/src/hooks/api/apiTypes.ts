export type RequestStatus = 'init' | 'loading' | 'success' | 'fail' | 'reloading'

export type DashboardViewModel = {
  stages: StageSearchViewModel[]
  songs: SongViewModel[]
  sequences: SequenceSearchViewModel[]
  playlists: PlaylistSearchViewModel[]
  scheduledTasks: ScheduledJobSearchViewModel[]
}

export type StageSearchViewModel = {
  id: number,
  name: string
}

export type SongViewModel = {
  id: number
  name: string
  artist: string
  durationMs: number
}

export type SequenceSearchViewModel = {
  id: number
  name: string
  songName: string
  stageName: string
  framesPerSecond: number
  durationMs: number
  status: SequenceStatus
}

export type SequenceStatus = 'NEW' | 'DRAFT' | 'PUBLISHED'

export type PlaylistViewModel = {
  id: number
  name: string
}

export type PlaylistSearchViewModel = {
  id: number
  name: string
  sequenceCount: number
  durationMs: number
}

export type ScheduledJobSearchViewModel = {
  id: number
  type: ScheduledJobAction
  cronExpression: string
  playlistName: string
  playlistId: number
}

export type ScheduledJobAction = 'NONE' | 'PLAY_PLAYLIST' | 'STOP_PLAYBACK' | 'SET_BRIGHTNESS'
