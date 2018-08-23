import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchSongs, showAddModal } from './actions';
import { fetchStages } from '../StageList/actions';
import AddSongModal from './components/AddSongModal';
import DeleteSongModal from './components/DeleteSongModal';
import SongEntry from './components/SongEntry';

class SongListPage extends Component {

  state = { searchQuery: '' };

  componentDidMount() {
    this.props.fetchSongs();
    this.props.fetchStages();
  }

  componentWillReceiveProps(nextProps) {
    const { props } = this;
    if (props.deleting && !nextProps.deleting && !nextProps.deleteError) {
      Alert.success('Song deleted successfully');
      nextProps.fetchSongs();
    } else if (props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Song added successfully');
      nextProps.fetchSongs();
    }
  }

  render() {
    const pageBody = (
      <div className="container">
        <div className="row">
          <div className="col-lg-12 input-group input-group-lg my-4">
            <input type="text" className="form-control" placeholder="Search..." value={this.state.searchQuery}
                   onChange={e => this.setState({ searchQuery: e.target.value })}/>
          </div>
        </div>

        <div className="row">
          <div className="col-lg-12">{this.renderContent()}</div>
        </div>

        <AddSongModal stages={this.props.stages}/>
        <DeleteSongModal/>
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
  }

  renderNavbar() {
    const canAdd = this.props.songs && this.props.stages;
    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={canAdd ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.props.showAddModal}>Add Song</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetching, fetchingStages, fetchError, fetchStagesError } = this.props;

    if (fetching || fetchingStages) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError(`Failed to load songs: ${fetchError}`);
    } else if (fetchStagesError) {
      return this.renderError(`Failed to load stages: ${fetchStagesError}`);
    } else {
      return this.renderSongs();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError(error) {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>{error}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderSongs() {
    if (_.isEmpty(this.props.songs)) {
      return (
        <div className="card border-info">
          <div className="card-body">
            No songs have been added.
          </div>
        </div>
      );
    }

    const songs = _(this.props.songs)
      .filter(this.songMatchesSearch.bind(this))
      .map(song => (
        <div key={song.id} className="col-md-6 col-lg-4 mb-4">
          <SongEntry song={song}/>
        </div>
      ))
      .value();

    return <div className="row">{songs}</div>;
  }

  songMatchesSearch(song) {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();

    if (!searchQuery) {
      return true;
    }

    const { name, album, artist } = song;
    return _.filter([name, album, artist], field => _.includes(field.toLowerCase(), searchQuery)).length > 0;
  }
}

function mapStateToProps({ page: { songList, stageList } }) {
  return {
    songs: songList.songs,
    fetching: songList.fetching,
    fetchError: songList.fetchError,
    stages: stageList.stages,
    fetchingStages: stageList.fetching,
    fetchStagesError: stageList.fetchError,
    adding: songList.adding,
    addError: songList.addError,
    deleting: songList.deleting,
    deleteError: songList.deleteError
  };
}

export default connect(mapStateToProps, { showAddModal, fetchSongs, fetchStages })(SongListPage);
