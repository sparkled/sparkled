import { ScheduledActionType } from '@/src/types/viewModels'

export const scheduledActionTypeLabel: Record<ScheduledActionType, string> = {
  NONE: 'No action',
  PLAY_PLAYLIST: 'Play playlist',
  STOP_PLAYBACK: 'Stop playback',
  SET_BRIGHTNESS: 'Set brightness',
}
