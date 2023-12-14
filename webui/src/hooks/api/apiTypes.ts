export type RequestStatus = 'init' | 'loading' | 'success' | 'fail' | 'reloading'

export type DashboardViewModel = {
  stages: StageSearchViewModel[]
  songs: SongViewModel[]
  sequences: SequenceSearchViewModel[]
  playlists: PlaylistSearchViewModel[]
  scheduledTasks: ScheduledJobSearchViewModel[]
}

export type StageSearchViewModel = {
  id: string
  name: string
}

export type SongViewModel = {
  id: string
  name: string
  artist: string
  durationMs: number
}

export type SequenceSearchViewModel = {
  id: string
  name: string
  songName: string
  stageName: string
  framesPerSecond: number
  durationMs: number
  status: SequenceStatus
}

export type SequenceStatus = 'NEW' | 'DRAFT' | 'PUBLISHED'

export type PlaylistViewModel = {
  id: string
  name: string
}

export type PlaylistSearchViewModel = {
  id: string
  name: string
  sequenceCount: number
  durationMs: number
}

export type ScheduledJobSearchViewModel = {
  id: string
  type: ScheduledJobAction
  cronExpression: string
  playlistName: string
  playlistId: number
}

export type ScheduledJobAction = 'NONE' | 'PLAY_PLAYLIST' | 'STOP_PLAYBACK' | 'SET_BRIGHTNESS'
