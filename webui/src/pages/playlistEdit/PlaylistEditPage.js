import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { ActionCreators } from 'redux-undo';
import Alert from 'react-s-alert';
import { Nav, NavItem, Table } from 'reactstrap';
import AddSequenceModal from './components/AddSequenceModal';
import PlaylistSequenceRow from './components/PlaylistSequenceRow';
import { setCurrentPage }  from '../actions';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchPlaylist, fetchSequences, savePlaylist, showAddSequenceModal } from './actions';

const { undo, redo, clearHistory } = ActionCreators;

class PlaylistEditPage extends Component {

  render() {
    const pageBody = (
      <div className="d-flex w-100 h-100">
        {this.renderContent()}
      </div>
    );

    return <PageContainer navbar={this.renderNavbar()}>{pageBody}</PageContainer>;
  }

  componentWillReceiveProps(nextProps) {
    const didSave = this.props.saving && !nextProps.saving;
    if (didSave) {
      const saveError = nextProps.saveError;
      if (saveError) {
        Alert.error(`Save failed: ${saveError}`);
      } else {
        Alert.success('Playlist saved successfully');
      }
    }
  }

  componentDidMount() {
    const { playlistId } = this.props.match.params;
    this.props.setCurrentPage({ pageTitle: 'Edit Playlist', pageClass: 'PlaylistEditPage' });
    this.props.fetchPlaylist(playlistId);
    this.props.fetchSequences();
  }

  renderNavbar() {
    const { canUndo, canRedo, undo, redo, playlist, sequences, savePlaylist, saving } = this.props;
    const loaded = playlist && sequences;

    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={(!saving && loaded && canUndo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => undo()}>Undo</span>
        </NavItem>
        <NavItem className={(!saving && loaded && canRedo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => redo()}>Redo</span>
        </NavItem>
        <NavItem className={!saving && loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.props.showAddSequenceModal}>Add Sequence</span>
        </NavItem>
        <NavItem className={!saving && loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => savePlaylist(playlist)}>Save</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetchError, fetching, playlist, sequences } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError();
    } else if (playlist && sequences) {
      return this.renderEditor();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError() {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>Failed to load playlist: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderEditor() {
    const { playlist } = this.props;

    return (
      <div className="container">
        <div className="row">
          <div className="col-lg-12">
            <h4 className="my-4">Edit playlist: {playlist.name}</h4>
            <Table bordered striped hover>
              <tbody>
                {_.map(playlist.sequences, this.renderPlaylistSequence)}
              </tbody>
            </Table>
          </div>
        </div>

        <AddSequenceModal/>
      </div>
    );
  }

  renderPlaylistSequence(playlistSequence) {
    return (
      <PlaylistSequenceRow key={playlistSequence.uuid}
                           form={`playlistSequence_${playlistSequence.uuid}`}
                           playlistSequence={playlistSequence}/>
    );
  }

  componentWillUnmount() {
    this.props.clearHistory();
  }
}

function mapStateToProps({ page }) {
  const { past, present, future } = page.playlistEdit;

  return {
    fetching: present.fetchingPlaylist || present.fetchingSequences,
    fetchError: present.fetchPlaylistError || present.fetchSequencesError,
    saving: present.saving,
    saveError: present.saveError,
    playlist: present.playlist,
    sequences: present.sequences,
    canUndo: past.length > 1,
    canRedo: future.length > 0
  };
}

export default connect(mapStateToProps, {
  setCurrentPage, fetchPlaylist, fetchSequences, showAddSequenceModal, savePlaylist, undo, redo, clearHistory
})(PlaylistEditPage);
