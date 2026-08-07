'use client'

import { AddPlaylistModal } from '@/components/dashboard/AddPlaylistModal'
import { AddScheduledTaskModal } from '@/components/dashboard/AddScheduledTaskModal'
import { AddSequenceModal } from '@/components/dashboard/AddSequenceModal'
import { AddSongModal } from '@/components/dashboard/AddSongModal'
import { AddStageModal } from '@/components/dashboard/AddStageModal'
import { DashboardPanel } from '@/components/dashboard/DashboardPanel'
import { DashboardPanelItem } from '@/components/dashboard/DashboardPanelItem'
import { PlaybackActions } from '@/components/dashboard/PlaybackActions'
import { title } from '@/components/primitives'
import { useApiGetDashboard } from '@/hooks/api/useApi'
import { SequenceStatus } from '@/src/types/viewModels'
import { formatDuration } from '@/utils/format'
import { scheduledActionTypeLabel } from '@/utils/labels'
import { Alert, Chip } from '@heroui/react'
import { CalendarClock, ListMusic, Music, SparklesIcon, TheaterIcon } from 'lucide-react'

const sequenceStatusColor: Record<SequenceStatus, 'default' | 'warning' | 'success'> = {
  NEW: 'default',
  DRAFT: 'warning',
  PUBLISHED: 'success',
}

export default function Home() {
  const { data: dashboard, error, isLoading } = useApiGetDashboard()

  return (
    <section className='flex flex-col gap-6 py-8'>
      <div className='flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center'>
        <h1 className={title({ size: 'sm' })}>Dashboard</h1>
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
          addAction={<AddStageModal />}
          count={dashboard?.stages.length}
          emptyLabel='No stages yet.'
          icon={<TheaterIcon size={20} />}
          isLoading={isLoading}
          items={dashboard?.stages ?? []}
          title='Stages'
          viewAllHref='/stages'
          renderItem={stage => <DashboardPanelItem title={stage.name} />}
        />

        <DashboardPanel
          addAction={<AddSongModal />}
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
          addAction={<AddSequenceModal />}
          count={dashboard?.sequences.length}
          emptyLabel='No sequences yet.'
          icon={<SparklesIcon size={20} />}
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
          addAction={<AddPlaylistModal />}
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
          addAction={<AddScheduledTaskModal />}
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
                  : scheduledActionTypeLabel[task.type]
              }
            />
          )}
        />
      </div>
    </section>
  )
}
