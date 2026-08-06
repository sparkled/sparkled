'use client'

import { BrightnessControl } from '@/components/dashboard/BrightnessControl'
import { DashboardPanel } from '@/components/dashboard/DashboardPanel'
import { DashboardPanelItem } from '@/components/dashboard/DashboardPanelItem'
import { PlaybackActions } from '@/components/dashboard/PlaybackActions'
import { title } from '@/components/primitives'
import { useApiGetDashboard } from '@/hooks/api/useApi'
import { ScheduledActionType, SequenceStatus } from '@/src/types/viewModels'
import { formatDuration } from '@/utils/format'
import { Alert, Chip } from '@heroui/react'
import { AudioLines, CalendarClock, Home as HomeIcon, ListMusic, Music } from 'lucide-react'

const sequenceStatusColor: Record<SequenceStatus, 'default' | 'warning' | 'success'> = {
  NEW: 'default',
  DRAFT: 'warning',
  PUBLISHED: 'success',
}

const scheduledTaskTypeLabel: Record<ScheduledActionType, string> = {
  NONE: 'No action',
  PLAY_PLAYLIST: 'Play playlist',
  STOP_PLAYBACK: 'Stop playback',
  SET_BRIGHTNESS: 'Set brightness',
}

export default function Home() {
  const { data: dashboard, error, isLoading } = useApiGetDashboard()

  return (
    <section className='flex flex-col gap-6 py-8'>
      <div className='flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center'>
        <h1 className={title({ size: 'sm' })}>Dashboard</h1>
        <BrightnessControl />
      </div>

      {error && (
        <Alert status='danger'>
          <Alert.Indicator />
          <Alert.Content>
            <Alert.Title>Unable to load dashboard</Alert.Title>
            <Alert.Description>
              Something went wrong while loading the dashboard data. Please try again later.
            </Alert.Description>
          </Alert.Content>
        </Alert>
      )}

      <div className='grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3'>
        <DashboardPanel
          count={dashboard?.stages.length}
          emptyLabel='No stages yet.'
          icon={<HomeIcon size={20} />}
          isLoading={isLoading}
          items={dashboard?.stages ?? []}
          title='Stages'
          viewAllHref='/stages'
          renderItem={stage => <DashboardPanelItem title={stage.name} />}
        />

        <DashboardPanel
          count={dashboard?.songs.length}
          emptyLabel='No songs yet.'
          icon={<Music size={20} />}
          isLoading={isLoading}
          items={dashboard?.songs ?? []}
          title='Songs'
          viewAllHref='/songs'
          renderItem={song => (
            <DashboardPanelItem
              subtitle={`${song.artist ?? 'Unknown artist'} · ${formatDuration(song.durationMs)}`}
              title={song.name}
            />
          )}
        />

        <DashboardPanel
          count={dashboard?.sequences.length}
          emptyLabel='No sequences yet.'
          icon={<AudioLines size={20} />}
          isLoading={isLoading}
          items={dashboard?.sequences ?? []}
          title='Sequences'
          viewAllHref='/sequences'
          renderItem={sequence => (
            <DashboardPanelItem
              actions={<PlaybackActions sequenceId={sequence.id} />}
              statusChip={
                <Chip color={sequenceStatusColor[sequence.status]} size='sm'>
                  {sequence.status}
                </Chip>
              }
              subtitle={`${sequence.stageName} · ${sequence.songName} · ${formatDuration(sequence.durationMs)}`}
              title={sequence.name}
            />
          )}
        />

        <DashboardPanel
          count={dashboard?.playlists.length}
          emptyLabel='No playlists yet.'
          icon={<ListMusic size={20} />}
          isLoading={isLoading}
          items={dashboard?.playlists ?? []}
          title='Playlists'
          viewAllHref='/playlists'
          renderItem={playlist => (
            <DashboardPanelItem
              actions={<PlaybackActions playlistId={playlist.id} />}
              subtitle={`${playlist.sequenceCount} sequence(s) · ${formatDuration(playlist.durationMs)}`}
              title={playlist.name}
            />
          )}
        />

        <DashboardPanel
          count={dashboard?.scheduledTasks.length}
          emptyLabel='No scheduled tasks yet.'
          icon={<CalendarClock size={20} />}
          isLoading={isLoading}
          items={dashboard?.scheduledTasks ?? []}
          title='Scheduled Tasks'
          viewAllHref='/scheduled-tasks'
          renderItem={task => (
            <DashboardPanelItem
              subtitle={`Cron (${task.cronExpression})`}
              title={
                task.type === 'PLAY_PLAYLIST' && task.playlistName
                  ? `Play playlist ${task.playlistName}`
                  : scheduledTaskTypeLabel[task.type]
              }
            />
          )}
        />
      </div>
    </section>
  )
}
