import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchSongs } from '../../services/song/actions';
import SongEntry from './components/SongEntry';

class SongListPage extends Component {

  constructor(props) {
    super(props);
    this.state = {
      searchQuery: ''
    };
  }

  componentDidMount() {
    this.props.fetchSongs();
  }

  render() {
    const { loading, error } = this.props;

    if (loading) {
      return <div>Loading...</div>;
    } else if (error) {
      return <div>{error}</div>;
    }

    return (
      <div>
        <div className="container">
          <div className="row">
            <div className="col-lg-12 input-group input-group-lg my-4">
              <input type="text" className="form-control" placeholder="Search..." value={this.state.searchQuery} onChange={e => this.setState({searchQuery: e.target.value})}/>
            </div>
          </div>

          <div className="row">
            {this.renderSongs()}
          </div>
        </div>
      </div>
    );
  }

  renderSongs() {
    const { songs } = this.props;
    return _(songs)
      .filter(this.songMatchesSearch.bind(this))
      .map(song => (
        <div className="col-md-6 col-lg-4 mb-4">
          <SongEntry key={song.id} song={song}/>
        </div>
      ))
      .value();
  }

  songMatchesSearch(song) {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();
    const { name, album, artist } = song;

    if (!searchQuery.trim()) {
      return true;
    }

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
