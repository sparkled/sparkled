import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import { setCurrentPage } from '../actions';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchPlaylists, showAddModal } from './actions';
import AddPlaylistModal from './components/AddPlaylistModal';
import DeletePlaylistModal from './components/DeletePlaylistModal';
import PlaylistCard from './components/PlaylistCard';

class PlaylistListPage extends Component {

  state = { searchQuery: '' };

  constructor(props) {
    super(props);
    this.playlistMatchesSearch = this.playlistMatchesSearch.bind(this);
  }

  componentDidMount() {
    this.props.setCurrentPage({ pageTitle: 'Playlists', pageClass: 'PlaylistListPage' });
    this.props.fetchPlaylists();
  }

  componentWillReceiveProps(nextProps) {
    const { props } = this;
    if (props.deleting && !nextProps.deleting && !nextProps.deleteError) {
      Alert.success('Playlist deleted successfully');
      nextProps.fetchPlaylists();
    } else if (props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Playlist added successfully');
      nextProps.fetchPlaylists();
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

        <AddPlaylistModal/>
        <DeletePlaylistModal/>
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
  }

  renderNavbar() {
    const canAdd = this.props.playlists;
    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={canAdd ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.props.showAddModal}>Add Playlist</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetching, fetchError } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError(`Failed to load playlists: ${fetchError}`);
    } else {
      return this.renderPlaylists();
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

  renderPlaylists() {
    if (_.isEmpty(this.props.playlists)) {
      return (
        <div className="card border-info">
          <div className="card-body">
            No playlists have been added.
          </div>
        </div>
      );
    }

    const playlists = _(this.props.playlists)
      .filter(this.playlistMatchesSearch)
      .map(playlist => (
        <div key={playlist.id} className="col-md-6 col-lg-4 mb-4">
          <PlaylistCard playlist={playlist}/>
        </div>
      ))
      .value();

    return <div className="row">{playlists}</div>;
  }

  playlistMatchesSearch(playlist) {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();
    return !searchQuery || _.includes(playlist.name.toLowerCase(), searchQuery);
  }
}

function mapStateToProps({ page: { playlistList } }) {
  return {
    fetching: playlistList.fetching,
    fetchError: playlistList.fetchError,
    playlists: playlistList.playlists,
    adding: playlistList.adding,
    addError: playlistList.addError,
    deleting: playlistList.deleting,
    deleteError: playlistList.deleteError
  };
}

export default connect(mapStateToProps, { setCurrentPage, showAddModal, fetchPlaylists })(PlaylistListPage);
