import _ from 'lodash'
import React, { Component } from 'react'
import { connect } from 'react-redux'
import { ActionCreators } from 'redux-undo'
import Alert from 'react-s-alert'
import { Table } from 'reactstrap'
import AddSequenceModal from './components/AddSequenceModal'
import PlaylistSequenceRow from './components/PlaylistSequenceRow'
import { setCurrentPage } from '../actions'
import LoadingIndicator from '../../components/LoadingIndicator'
import PageContainer from '../../components/PageContainer.jsx'
import { fetchPlaylist, fetchSequences, savePlaylist, showAddSequenceModal } from './actions'
import { IconButton } from '@material-ui/core'
import { Add, Redo, Save, Undo } from '@material-ui/icons'
import { getErrorMessage } from '../../utils/errorUtils'

const { undo, redo, clearHistory } = ActionCreators

class PlaylistEditPage extends Component {
  render() {
    const pageBody = <div className='d-flex w-100 h-100'>{this.renderContent()}</div>

    return <PageContainer actions={this.renderAction()}>{pageBody}</PageContainer>
  }

  componentWillReceiveProps(nextProps) {
    const didSave = this.props.saving && !nextProps.saving
    if (didSave) {
      const saveError = nextProps.saveError
      if (saveError) {
        Alert.error(`Save failed: ${getErrorMessage(saveError)}`)
      } else {
        Alert.success('Playlist saved successfully')
      }
    }
  }

  componentDidMount() {
    const { playlistId } = this.props.match.params
    this.props.setCurrentPage({
      pageTitle: 'Edit Playlist',
      pageClass: 'PlaylistEditPage',
    })
    this.props.fetchPlaylist(playlistId)
    this.props.fetchSequences()
  }

  renderAction() {
    const { canUndo, canRedo, undo, redo, playlist, sequences, savePlaylist, saving } = this.props
    const loaded = playlist && sequences

    return (
      <>
        <IconButton onClick={() => undo()} disabled={saving || !loaded || !canUndo} title='Undo'>
          <Undo />
        </IconButton>

        <IconButton onClick={() => redo()} disabled={saving || !loaded || !canRedo} title='Redo'>
          <Redo />
        </IconButton>

        <IconButton onClick={this.props.showAddSequenceModal} disabled={saving || !loaded} title='Add sequence'>
          <Add />
        </IconButton>

        <IconButton onClick={() => savePlaylist(playlist)} disabled={saving || !loaded} title='Save playlist'>
          <Save />
        </IconButton>
      </>
    )
  }

  renderContent() {
    const { fetchError, fetching, playlist, sequences } = this.props

    if (fetching) {
      return this.renderLoading()
    } else if (fetchError) {
      return this.renderError()
    } else if (playlist && sequences) {
      return this.renderEditor()
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100} />
  }

  renderError() {
    return (
      <div className='card border-danger'>
        <div className='card-body'>
          <p>Failed to load playlist: {this.props.fetchError}</p>
          <button className='btn btn-danger' onClick={() => window.location.reload()}>
            Reload the page
          </button>
        </div>
      </div>
    )
  }

  renderEditor() {
    const { playlist } = this.props

    return (
      <div className='container'>
        <div className='row'>
          <div className='col-lg-12'>
            <h4 className='my-4'>Edit playlist: {playlist.name}</h4>
            <Table bordered striped hover>
              <tbody>{_.map(playlist.sequences, this.renderPlaylistSequence)}</tbody>
            </Table>
          </div>
        </div>

        <AddSequenceModal />
      </div>
    )
  }

  renderPlaylistSequence(playlistSequence) {
    return (
      <PlaylistSequenceRow
        key={playlistSequence.id}
        form={`playlistSequence_${playlistSequence.id}`}
        playlistSequence={playlistSequence}
      />
    )
  }

  componentWillUnmount() {
    this.props.clearHistory()
  }
}

function mapStateToProps({ page }) {
  const { past, present, future } = page.playlistEdit

  return {
    fetching: present.fetchingPlaylist || present.fetchingSequences,
    fetchError: present.fetchPlaylistError || present.fetchSequencesError,
    saving: present.saving,
    saveError: present.saveError,
    playlist: present.playlist,
    sequences: present.sequences,
    canUndo: past.length > 1,
    canRedo: future.length > 0,
  }
}

export default connect(mapStateToProps, {
  setCurrentPage,
  fetchPlaylist,
  fetchSequences,
  showAddSequenceModal,
  savePlaylist,
  undo,
  redo,
  clearHistory,
})(PlaylistEditPage)
