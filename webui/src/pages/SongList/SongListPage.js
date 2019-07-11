import { Card, CardContent, Grid, withStyles } from '@material-ui/core';
import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import PageContainer from '../../components/PageContainer';
import SearchBar from '../../components/SearchBar';
import { setCurrentPage } from '../actions';
import { fetchSongs, showAddModal } from './actions';
import AddSongModal from './components/AddSongModal';
import DeleteSongModal from './components/DeleteSongModal';
import SongCard from './components/SongCard';

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  emptyCard: {
    margin: 'auto'
  }
});

class SongListPage extends Component {

  state = { searchQuery: '' };

  componentDidMount() {
    this.props.setCurrentPage({ pageTitle: 'Songs', pageClass: 'SongListPage' });
    this.props.fetchSongs();
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
    const { classes, fetching, showAddModal } = this.props;

    const pageBody = (
      <div className={classes.root}>
        <SearchBar placeholderText="Search songs" fetching={fetching} onAddButtonClick={showAddModal}
                   onSearch={this.filterSongs}
        />

        <Grid container spacing={3}>
          {this.renderContent()}
        </Grid>

        <AddSongModal/>
        <DeleteSongModal/>
      </div>
    );

    return <PageContainer body={pageBody}/>;
  }

  renderContent() {
    const { fetching, fetchError } = this.props;

    if (fetching) {
      return [];
    } else if (fetchError) {
      return this.renderError(`Failed to load songs: ${fetchError}`);
    } else {
      const filteredSongs = this.getFilteredSongs();
      if (_.isEmpty(filteredSongs)) {
        return this.renderEmpty();
      } else {
        return this.renderSongs(filteredSongs);
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
          No songs found.
        </CardContent>
      </Card>
    );
  }

  renderSongs(songs) {
    return _.map(songs, song => (
      <Grid item key={song.id} xs={12} sm={6} md={4}>
        <SongCard song={song}/>
      </Grid>
    ));
  }

  filterSongs = searchQuery => this.setState({ searchQuery });

  getFilteredSongs = () => _.filter(this.props.songs || [], this.songMatchesSearch);

  songMatchesSearch = song => {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();

    if (!searchQuery) {
      return true;
    }

    const { name, album, artist } = song;
    return _.filter([name, album, artist], field => _.includes(field.toLowerCase(), searchQuery)).length > 0;
  }
}

function mapStateToProps({ page: { songList } }) {
  return {
    songs: songList.songs,
    fetching: songList.fetching,
    fetchError: songList.fetchError,
    adding: songList.adding,
    addError: songList.addError,
    deleting: songList.deleting,
    deleteError: songList.deleteError
  };
}

SongListPage = connect(mapStateToProps, { setCurrentPage, showAddModal, fetchSongs })(SongListPage);
export default withStyles(styles)(SongListPage);
