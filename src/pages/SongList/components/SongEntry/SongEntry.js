import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { showDeleteModal } from '../../actions';
import './SongEntry.css';

class SongEntry extends Component {

  state = { dropdownOpen: false };

  render() {
    const { song } = this.props;

    return (
      <div className="song-entry card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h4 className="mb-0">{song.name}</h4>

          <div className="btn-group">
            <Link className="btn btn-secondary" to={`/songs/${song.id}`}>Edit</Link>

            <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle.bind(this)}>
              <DropdownToggle caret/>
              <DropdownMenu right>
                <DropdownItem onClick={this.deleteSong.bind(this)}>Delete Song</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </div>

        <div className="card-block">
          <div className="d-flex justify-content-between">
            <h6>Album: {song.album}</h6>
            <h6 className="ml-3">{this.getFormattedDuration(song)}</h6>
          </div>

          <div className="d-flex justify-content-between">
            <h6>Artist: {song.artist}</h6>
            <div className="ml-3">
              <span className="badge badge-primary">{song.status}</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ dropdownOpen: !prevState.dropdownOpen }));
  }

  deleteSong() {
    const { showDeleteModal, song } = this.props;
    showDeleteModal(song);
  }

  getFormattedDuration(song) {
    const date = new Date(null);
    date.setSeconds(Math.round(song.durationFrames / song.framesPerSecond));

    // 1970-01-01T00:01:23.000Z
    //               ^^^^^
    return date.toISOString().substr(14, 5);
  }
}

function mapStateToProps({ data: { songs }, page: { songList } }) {
  return {
    songToDelete: songList.songToDelete,
    deleting: songs.deleting
  };
}

export default connect(mapStateToProps, { showDeleteModal })(SongEntry);
