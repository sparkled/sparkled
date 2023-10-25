import axios from 'axios'
import { useMemo } from 'react'
import { Col, Container, Row } from 'react-grid-system'
import { useDispatch } from 'react-redux'
import { Link } from 'react-router-dom'
import styled from 'styled-components'
import AppBar from '../../components/AppBar'
import PageLoadingContainer from '../../components/PageLoadingContainer'
import * as restConfig from '../../config/restConfig'
import useApiGetDashboard from '../../hooks/api/useApiGetDashboard'
import useModal from '../../hooks/useModal'
import { playPlaylist, stopPlaylist } from '../../pages/playlistList/actions'
import { showAddScheduledTaskModal, showDeleteScheduledTaskModal } from '../../pages/scheduler/actions'
import AddScheduledJobModal from '../../pages/scheduler/components/AddScheduledJobModal/AddScheduledJobModal'
import DeleteScheduledJobModal from '../../pages/scheduler/components/DeleteScheduledJobModal/DeleteScheduledJobModal'
import { showAddSequenceModal, showDeleteSequenceModal } from '../../pages/sequenceList/actions'
import AddSequenceModal from '../../pages/sequenceList/components/AddSequenceModal/AddSequenceModal'
import DeleteSequenceModal from '../../pages/sequenceList/components/DeleteSequenceModal/DeleteSequenceModal'
import { showAddSongModal, showDeleteSongModal } from '../../pages/songList/actions'
import AddSongModal from '../../pages/songList/components/AddSongModal/AddSongModal'
import DeleteSongModal from '../../pages/songList/components/DeleteSongModal/DeleteSongModal'
import { showAddStageModal, showDeleteStageModal } from '../../pages/stageList/actions'
import AddStageModal from '../../pages/stageList/components/AddStageModal'
import DeleteStageModal from '../../pages/stageList/components/DeleteStageModal'
import { useAppSelector } from '../../store/store'
import { getFormattedDuration } from '../../utils/dateUtils'
import AddPlaylistModal from './AddPlaylistModal'
import DashboardSwimlane from './DashboardSwimlane'
import DashboardSwimlaneItem from './DashboardSwimlaneItem'
import DeletePlaylistModal from './DeletePlaylistModal'

const S = {
  PageLoadingContainer: styled(PageLoadingContainer)`
    overflow: hidden;
  `,

  Container: styled.div`
    height: 100%;
    overflow: hidden;
  `,

  GridArea: styled.div<{ area: string }>`
    display: grid;
    grid-area: ${p => p.area};
    background: #414141;
    border-radius: 5px;
    width: 100%;
    height: 100%;
  `,

  PanelGrid: styled.div`
    display: grid;
    flex: 1;
    gap: 32px;
    margin: 32px 0;
    grid-template-rows: minmax(0, 1fr);
    grid-template-columns: repeat(5, minmax(0, 1fr));
    grid-template-areas: 'stages songs sequences playlists tasks';

    @media (max-width: 768px) {
      gap: 16px;
      grid-template-rows: repeat(5, minmax(0, 1fr));
      grid-template-columns: minmax(0, 1fr);
      grid-template-areas:
        'stages'
        'songs'
        'sequences'
        'playlists'
        'tasks';
    }

    @media (min-width: 769px) and (max-width: 992px) {
      grid-template-rows: repeat(2, minmax(0, 1fr));
      grid-template-columns: repeat(3, minmax(0, 1fr));
      grid-template-areas:
        'stages sequences songs'
        'tasks  sequences playlists';
    }

    @media (min-width: 993px) and (max-width: 1140px) {
      grid-template-rows: repeat(2, minmax(0, 1fr));
      grid-template-columns: repeat(4, minmax(0, 1fr));
      grid-template-areas:
        'stages songs sequences playlists'
        'tasks  songs sequences playlists';
    }
  `,

  DropdownItem: styled.div`
    padding: 8px;
    transition: background 0.2s;

    &:hover {
      cursor: pointer;
      background: rgba(0, 0, 0, 0.1);
    }

    * {
      color: #fff !important;
      text-decoration: none;
    }
  `
}

const DashboardScreen = () => {
  useApiGetDashboard()
  const addPlaylistModal = useModal('playlistAdd')
  const deletePlaylistModal = useModal('playlistDelete')
  const { dashboard, query, requestStatus } = useAppSelector(state => state.dashboardScreen)
  const searchQuery = query.trim().toLowerCase()
  const dispatch = useDispatch()

  const stageItems = useMemo(() => {
    return (dashboard?.stages ?? [])
      .filter(it => it.name.toLowerCase().includes(searchQuery))
      .map(it => {
        const actions = (
          <>
            <S.DropdownItem>
              <Link to={`/stages/${it.id}`}>Edit Stage</Link>
            </S.DropdownItem>
            <S.DropdownItem onClick={() => dispatch(showDeleteStageModal(it))}>Delete Stage</S.DropdownItem>
          </>
        )

        return <DashboardSwimlaneItem key={it.id} title={it.name} actions={actions} />
      })
  }, [dashboard, dispatch, searchQuery])

  const songItems = useMemo(() => {
    return (dashboard?.songs ?? [])
      .filter(
        it =>
          it.name.toLowerCase().includes(searchQuery) ||
          it.artist.toLowerCase().includes(searchQuery)
      )
      .map(it => {
        const actions = (
          <>
            <S.DropdownItem onClick={() => dispatch(showDeleteSongModal(it))}>Delete Song</S.DropdownItem>
          </>
        )

        return (
          <DashboardSwimlaneItem
            key={it.id}
            title={it.name}
            subtitle={getFormattedDuration(it.durationMs / 1000)}
            actions={actions}
          />
        )
      })
  }, [dashboard, dispatch, searchQuery])

  const playSequence = async (sequenceId: number) => {
    const playlistAction = {
      action: 'PLAY_SEQUENCE',
      sequenceId: sequenceId
    }
    await axios.post(`${restConfig.ROOT_URL}/player`, playlistAction)
  }

  const stopSequence = async () => {
    const playlistAction = { action: 'STOP' }
    await axios.post(`${restConfig.ROOT_URL}/player`, playlistAction)
  }

  const sequenceItems = useMemo(() => {
    return (dashboard?.sequences ?? [])
      .filter(it => it.name.toLowerCase().includes(searchQuery))
      .map(it => {
        const actions = (
          <>
            <S.DropdownItem onClick={() => playSequence(it.id)}>Play Sequence</S.DropdownItem>
            <S.DropdownItem onClick={() => stopSequence()}>Stop Sequence</S.DropdownItem>
            <S.DropdownItem>
              <Link to={`/sequences/${it.id}`}>Edit Sequence</Link>
            </S.DropdownItem>
            <S.DropdownItem onClick={() => dispatch(showDeleteSequenceModal(it))}>Delete Sequence</S.DropdownItem>
          </>
        )

        return (
          <DashboardSwimlaneItem
            key={it.id}
            status={it.status === 'PUBLISHED' ? 'success' : 'danger'}
            title={it.name}
            subtitle={`${it.stageName} · ${it.songName} · ${getFormattedDuration(it.durationMs / 1000)}`}
            actions={actions}
          />
        )
      })
  }, [dashboard, dispatch, searchQuery])

  const playlistItems = useMemo(() => {
    return (dashboard?.playlists ?? [])
      .filter(it => it.name.toLowerCase().includes(searchQuery))
      .map(it => {
        const actions = (
          <>
            <S.DropdownItem onClick={() => dispatch(playPlaylist(it.id))}>Play Playlist</S.DropdownItem>
            <S.DropdownItem onClick={() => dispatch(stopPlaylist())}>Stop Playlist</S.DropdownItem>
            <S.DropdownItem>
              <Link to={`/playlists/${it.id}`}>Edit Playlist</Link>
            </S.DropdownItem>
            <S.DropdownItem onClick={() => deletePlaylistModal.show(it)}>Delete Playlist</S.DropdownItem>
          </>
        )

        const sequenceCount = `${it.sequenceCount} sequence${it.sequenceCount === 1 ? '' : 's'}`
        const totalDuration = getFormattedDuration(it.durationMs / 1000)
        return (
          <DashboardSwimlaneItem
            key={it.id}
            title={it.name}
            subtitle={`${sequenceCount} · ${totalDuration}`}
            actions={actions}
          />
        )
      })
  }, [dashboard?.playlists, deletePlaylistModal, dispatch, searchQuery])

  const scheduledTaskItems = useMemo(() => {
    return (dashboard?.scheduledTasks ?? [])
      .filter(it => it.type.toLowerCase().includes(searchQuery))
      .map(it => {
        const actions = (
          <>
            <S.DropdownItem onClick={() => dispatch(showDeleteScheduledTaskModal(it))}>
              Delete scheduled task
            </S.DropdownItem>
          </>
        )

        const trigger = `Cron (${it.cronExpression})`

        let action
        switch (it.type) {
          case 'PLAY_PLAYLIST':
            action = `Play playlist ${it.playlistName}`
            break
          case 'SET_BRIGHTNESS':
            action = `Set brightness`
            break
          case 'STOP_PLAYBACK':
            action = 'Stop playback'
            break
          default:
            action = 'Unknown action'
        }

        return <DashboardSwimlaneItem key={it.id} title={action} subtitle={trigger} actions={actions} />
      })
  }, [dashboard, dispatch, searchQuery])

  return (
    <>
      <AddStageModal />
      <DeleteStageModal />
      <AddSongModal />
      <DeleteSongModal />
      <AddSequenceModal songs={dashboard?.songs} stages={dashboard?.stages} />
      <DeleteSequenceModal />
      <AddPlaylistModal />
      <DeletePlaylistModal />
      <AddScheduledJobModal playlists={dashboard?.playlists} />
      <DeleteScheduledJobModal />

      <AppBar />
      <S.PageLoadingContainer loading={requestStatus === 'loading'}>
        <Container style={{ width: '100%' }}>
          <Row style={{ display: 'flex', height: '100%' }}>
            <Col style={{ display: 'flex', height: '100%' }}>
              <S.PanelGrid>
                <DashboardSwimlane gridArea='stages' title='Stages' onAdd={() => dispatch(showAddStageModal())}>
                  {stageItems}
                </DashboardSwimlane>
                <DashboardSwimlane gridArea='songs' title='Songs' onAdd={() => dispatch(showAddSongModal())}>
                  {songItems}
                </DashboardSwimlane>
                <DashboardSwimlane
                  gridArea='sequences'
                  title='Sequences'
                  onAdd={() => dispatch(showAddSequenceModal())}
                >
                  {sequenceItems}
                </DashboardSwimlane>
                <DashboardSwimlane
                  gridArea='playlists'
                  title='Playlists'
                  onAdd={addPlaylistModal.show}
                >
                  {playlistItems}
                </DashboardSwimlane>
                <DashboardSwimlane gridArea='tasks' title='Tasks' onAdd={() => dispatch(showAddScheduledTaskModal())}>
                  {scheduledTaskItems}
                </DashboardSwimlane>
              </S.PanelGrid>
            </Col>
          </Row>
        </Container>
      </S.PageLoadingContainer>
    </>
  )
}

export default DashboardScreen
