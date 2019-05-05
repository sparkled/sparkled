import { withStyles } from '@material-ui/core';
import Card from '@material-ui/core/es/Card/Card';
import CardContent from '@material-ui/core/es/CardContent/CardContent';
import Grid from '@material-ui/core/Grid/Grid';
import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import PageContainer from '../../components/PageContainer';
import SearchBar from '../../components/SearchBar';
import { setCurrentPage } from '../actions';
import { fetchPlaylists, showAddModal } from './actions';
import AddPlaylistModal from './components/AddPlaylistModal';
import DeletePlaylistModal from './components/DeletePlaylistModal';
import PlaylistCard from './components/PlaylistCard';

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  emptyCard: {
    margin: 'auto'
  }
});


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
    } else if (this.props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Playlist added successfully');
      nextProps.fetchPlaylists();
    }
  }

  render() {
    const { classes, fetching, showAddModal } = this.props;

    const pageBody = (
      <div className={classes.root}>
        <SearchBar placeholderText="Search playlists" fetching={fetching} onAddButtonClick={showAddModal}
                   onSearch={this.filterPlaylists}
        />

        <Grid container spacing={24}>
          {this.renderContent()}
        </Grid>

        <AddPlaylistModal/>
        <DeletePlaylistModal/>
      </div>
    );

    return <PageContainer body={pageBody}/>;
  }

  renderContent() {
    const { fetching, fetchError } = this.props;

    if (fetching) {
      return [];
    } else if (fetchError) {
      return this.renderError(`Failed to load playlists: ${fetchError}`);
    } else {
      const filteredPlaylists = this.getFilteredPlaylists();
      if (_.isEmpty(filteredPlaylists)) {
        return this.renderEmpty();
      } else {
        return this.renderPlaylists(filteredPlaylists);
      }
    }
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

  renderEmpty() {
    return (
      <Card className={this.props.classes.emptyCard}>
        <CardContent>
          No playlists found.
        </CardContent>
      </Card>
    );
  }

  renderPlaylists(playlists) {
    return _.map(playlists, playlist => (
      <Grid item key={playlist.id} xs={12} sm={6} md={4}>
        <PlaylistCard playlist={playlist}/>
      </Grid>
    ));
  }

  filterPlaylists = searchQuery => this.setState({ searchQuery });

  getFilteredPlaylists = () => _.filter(this.props.playlists || [], this.playlistMatchesSearch);

  playlistMatchesSearch = playlist => {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();
    return !searchQuery || _.includes(playlist.name.toLowerCase(), searchQuery);
  }
}

function mapStateToProps({ page: { playlistList } }) {
  return {
    playlists: playlistList.playlists,
    fetching: playlistList.fetching,
    fetchError: playlistList.fetchError,
    adding: playlistList.adding,
    addError: playlistList.addError,
    deleting: playlistList.deleting,
    deleteError: playlistList.deleteError
  };
}

PlaylistListPage = withStyles(styles)(PlaylistListPage);
export default connect(mapStateToProps, { setCurrentPage, showAddModal, fetchPlaylists })(PlaylistListPage);
