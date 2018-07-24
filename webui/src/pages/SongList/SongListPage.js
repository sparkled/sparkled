import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchSongs } from '../../services/song/actions';
import { showAddModal } from './actions';
import AddSongModal from './components/AddSongModal';
import DeleteSongModal from './components/DeleteSongModal';
import SongEntry from './components/SongEntry';

class SongListPage extends Component {

  state = { searchQuery: '' };

  componentDidMount() {
    this.props.fetchSongs();
  }

  componentWillReceiveProps(nextProps) {
    if (!this.props.deleteSuccess && nextProps.deleteSuccess) {
      Alert.success('Song deleted successfully');
      this.props.fetchSongs();
    } else if (!this.props.addSuccess && nextProps.addSuccess) {
      Alert.success('Song added successfully');
      this.props.fetchSongs();
    }
  }

  render() {
    const pageBody = (
      <div>
        <div className="row">
          <div className="col-lg-12 input-group input-group-lg my-4">
            <input type="text" className="form-control" placeholder="Search..." value={this.state.searchQuery}
                   onChange={e => this.setState({ searchQuery: e.target.value })}/>
          </div>
        </div>

        <div className="row">
          <div className="col-lg-12">{this.renderContent()}</div>
        </div>

        <AddSongModal/>
        <DeleteSongModal/>
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
  }

  renderNavbar() {
    return (
      <Nav className="ml-auto" navbar>
        <NavItem>
          <span className="nav-link" onClick={this.props.showAddModal}>Add Song</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetchError, fetching } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError();
    } else {
      return this.renderSongs();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError() {
    return (
      <div className="card card-outline-danger">
        <div className="card-block">
          <p>Failed to load songs: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderSongs() {
    if (_.isEmpty(this.props.songs)) {
      return (
        <div className="card card-outline-info">
          <div className="card-block">
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

function mapStateToProps({ data: { songs } }) {
  return {
    songs: songs.data,
    fetching: songs.fetching,
    fetchError: songs.fetchError,
    addSuccess: songs.addSuccess,
    deleteSuccess: songs.deleteSuccess
  };
}

export default connect(mapStateToProps, { showAddModal, fetchSongs })(SongListPage);