import { isEmpty, map } from 'lodash-es';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { fetchSongs } from '../../services/song/actions';

class SongListPage extends Component {

  componentDidMount() {
    this.props.fetchSongs();
  }

  render() {
    if (isEmpty(this.props.songs)) {
      return <div>Loading...</div>;
    }

    return (
      <div>
        <h2>Song List</h2>
        {this.renderSongs()}
      </div>
    );
  }

  renderSongs() {
    const { match } = this.props;

    return map(this.props.songs, song =>
      <div key={song.id}>
        <Link to={`${match.url}/${song.id}`}>Edit</Link>
        <div>{song.name}</div>
        <div>{song.artist}</div>
        <div>{song.album}</div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    songs: state.songs
  };
}

export default connect(mapStateToProps, { fetchSongs })(SongListPage);
