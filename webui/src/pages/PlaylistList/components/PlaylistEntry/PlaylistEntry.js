import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button, Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { showDeleteModal, playPlaylist } from '../../actions';
import { getFormattedDuration } from '../../../../utils/dateUtils';
import './PlaylistEntry.css';

class PlaylistEntry extends Component {

  state = { dropdownOpen: false };

  constructor(props) {
    super(props);
    this.toggle = this.toggle.bind(this);
    this.showDeleteModal = this.showDeleteModal.bind(this);
    this.playPlaylist = this.playPlaylist.bind(this);
  }

  render() {
    const { playlist } = this.props;

    return (
      <div className="PlaylistEntry card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h4 className="mb-0">{playlist.name}</h4>

          <div className="btn-group">
            <Link className="btn btn-secondary" to={`/playlists/${playlist.id}`}>Edit</Link>

            <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
              <DropdownToggle caret/>
              <DropdownMenu right>
                <DropdownItem onClick={this.showDeleteModal}>Delete playlist</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </div>

        <div className="card-body">
          <div className="d-flex justify-content-between">
            <h6>Name: {playlist.name}</h6>
            <Button color="info" size="sm" onClick={this.playPlaylist}>Play</Button>
          </div>

          <div className="d-flex justify-content-between">
            <h6>Sequences: {playlist.sequenceCount}</h6>
            <h6>Duration: {getFormattedDuration(playlist.durationSeconds)}</h6>
          </div>
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ dropdownOpen: !prevState.dropdownOpen }));
  }

  playPlaylist() {
    const { playPlaylist, playlist } = this.props;
    playPlaylist(playlist.id);
  }

  showDeleteModal() {
    const { showDeleteModal, playlist } = this.props;
    showDeleteModal(playlist);
  }
}

function mapStateToProps({ page: { playlistList } }) {
  return {
    playlistToDelete: playlistList.playlistToDelete,
    deleting: playlistList.deleting
  };
}

export default connect(mapStateToProps, { showDeleteModal, playPlaylist })(PlaylistEntry);
