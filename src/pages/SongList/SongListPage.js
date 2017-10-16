import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchSongs } from '../../services/song/actions';
import SongEntry from './components/SongEntry';

class SongListPage extends Component {

  state = { searchQuery: '' };

  componentDidMount() {
    this.props.fetchSongs();
  }

  render() {
    const { error, loading } = this.props;

    const pageBody = (
      <div>
        <div className="row">
          <div className="col-lg-12 input-group input-group-lg my-4">
            <input type="text" className="form-control" placeholder="Search..." value={this.state.searchQuery}
                   onChange={e => this.setState({ searchQuery: e.target.value })}/>
          </div>
        </div>

        <div className="row">
          {loading && this.renderLoading()}
          {error && this.renderError(error)}
          {this.renderSongs()}
          </div>
      </div>
    );

    return <PageContainer body={pageBody}/>;
  }

  renderLoading() {
    return (
      <div className="col-lg-12">
        <LoadingIndicator size={100}/>
      </div>
    );
  }

  renderError(error) {
    return (
      <div className="col-lg-12">
        <div className="card card-outline-danger">
          <div className="card-block">
            <p>Failed to load songs: {error}</p>
            <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
          </div>
        </div>
      </div>
    );
  }

  renderSongs() {
    return _(this.props.songs)
      .filter(this.songMatchesSearch.bind(this))
      .map(song => (
        <div key={song.id} className="col-md-6 col-lg-4 mb-4">
          <SongEntry song={song}/>
        </div>
      ))
      .value();
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

function mapStateToProps(state) {
  return {
    songs: state.songs.data,
    loading: state.songs.loading,
    error: state.songs.error
  };
}

export default connect(mapStateToProps, { fetchSongs })(SongListPage);
