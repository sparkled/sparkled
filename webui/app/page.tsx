'use client'

import { AddPlaylistModal } from '@/components/dashboard/AddPlaylistModal'
import { AddScheduledTaskModal } from '@/components/dashboard/AddScheduledTaskModal'
import { AddSequenceModal } from '@/components/dashboard/AddSequenceModal'
import { AddSongModal } from '@/components/dashboard/AddSongModal'
import { AddStageModal } from '@/components/dashboard/AddStageModal'
import { AddToPlaylistMenu } from '@/components/dashboard/AddToPlaylistMenu'
import { DashboardPanel } from '@/components/dashboard/DashboardPanel'
import { DashboardPanelItem } from '@/components/dashboard/DashboardPanelItem'
import { PlaybackActions } from '@/components/dashboard/PlaybackActions'
import { PlaylistActions } from '@/components/dashboard/PlaylistActions'
import { ScheduledTaskActionsMenu } from '@/components/dashboard/ScheduledTaskActionsMenu'
import { SequenceActions } from '@/components/dashboard/SequenceActions'
import { StageActions } from '@/components/dashboard/StageActions'
import {
  useApiGetPlaylists,
  useApiGetScheduledTasks,
  useApiGetSequences,
  useApiGetSongs,
  useApiGetStages,
} from '@/hooks/api/useApi'
import { getCronSummary } from '@/utils/cron'
import { formatDuration } from '@/utils/format'
import { scheduledActionTypeLabel, sequenceStatusColor } from '@/utils/labels'
import { Alert, Chip } from '@heroui/react'
import { CalendarClock, ListMusic, Music, SparklesIcon, TheaterIcon } from 'lucide-react'

export default function Home() {
  const { data: stages, error: stagesError, isLoading: stagesLoading } = useApiGetStages()
  const { data: songs, error: songsError, isLoading: songsLoading } = useApiGetSongs()
  const {
    data: sequences,
    error: sequencesError,
    isLoading: sequencesLoading,
  } = useApiGetSequences()
  const {
    data: playlists,
    error: playlistsError,
    isLoading: playlistsLoading,
  } = useApiGetPlaylists()
  const {
    data: scheduledTasks,
    error: scheduledTasksError,
    isLoading: scheduledTasksLoading,
  } = useApiGetScheduledTasks()

  const error = stagesError || songsError || sequencesError || playlistsError || scheduledTasksError

  return (
    <section className='flex flex-col gap-6 py-8'>
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
          count={stages?.length}
          emptyLabel='No stages yet.'
          icon={<TheaterIcon size={20} />}
          isLoading={stagesLoading}
          items={stages ?? []}
          title='Stages'
          viewAllHref='/stages'
          renderItem={stage => (
            <DashboardPanelItem actions={<StageActions stageId={stage.id} />} title={stage.name} />
          )}
        />

        <DashboardPanel
          addAction={<AddSongModal />}
          count={songs?.length}
          emptyLabel='No songs yet.'
          icon={<Music size={20} />}
          isLoading={songsLoading}
          items={songs ?? []}
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
          count={sequences?.length}
          emptyLabel='No sequences yet.'
          icon={<SparklesIcon size={20} />}
          isLoading={sequencesLoading}
          items={sequences ?? []}
          title='Sequences'
          viewAllHref='/sequences'
          renderItem={sequence => (
            <DashboardPanelItem
              actions={
                <>
                  <PlaybackActions sequenceId={sequence.id} />
                  <AddToPlaylistMenu sequenceId={sequence.id} />
                  <SequenceActions sequenceId={sequence.id} />
                </>
              }
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
          count={playlists?.length}
          emptyLabel='No playlists yet.'
          icon={<ListMusic size={20} />}
          isLoading={playlistsLoading}
          items={playlists ?? []}
          title='Playlists'
          viewAllHref='/playlists'
          renderItem={playlist => {
            let count = playlist.sequenceCount
            let countMessage = `${count} ${count === 1 ? 'sequence' : 'sequences'}`
            let duration = `${formatDuration(playlist.durationMs)}`
            return (
              <DashboardPanelItem
                actions={
                  <>
                    <PlaybackActions playlistId={playlist.id} />
                    <PlaylistActions playlistId={playlist.id} />
                  </>
                }
                subtitle={`${countMessage} · ${duration}`}
                title={playlist.name}
              />
            )
          }}
        />

        <DashboardPanel
          addAction={<AddScheduledTaskModal />}
          count={scheduledTasks?.length}
          emptyLabel='No scheduled tasks yet.'
          icon={<CalendarClock size={20} />}
          isLoading={scheduledTasksLoading}
          items={scheduledTasks ?? []}
          title='Scheduled Tasks'
          viewAllHref='/scheduled-tasks'
          renderItem={task => (
            <DashboardPanelItem
              actions={<ScheduledTaskActionsMenu taskId={task.id} />}
              subtitle={getCronSummary(task.cronExpression)}
              title={
                task.type === 'PLAY_PLAYLIST' && task.playlistName
                  ? `Play playlist: ${task.playlistName}`
                  : scheduledActionTypeLabel[task.type]
              }
            />
          )}
        />
      </div>
    </section>
  )
}
