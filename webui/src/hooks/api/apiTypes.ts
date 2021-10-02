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
  album: string
  durationMs: number
}

export type SequenceSearchViewModel = {
  id: number
  name: string
  songName: string
  stageName: string
  framesPerSecond: number
  durationSeconds: number
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
  durationSeconds: number
}

export type ScheduledJobSearchViewModel = {
  id: number
  action: ScheduledJobAction
  cronExpression: string
  playlistName: string
  playlistId: number
}

export type ScheduledJobAction = 'NONE' | 'PLAY_PLAYLIST' | 'STOP_PLAYBACK' | 'SET_BRIGHTNESS'
