import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { showDeleteModal } from '../../actions';
import './PlaylistEntry.css';

class PlaylistEntry extends Component {

  state = { dropdownOpen: false };

  render() {
    const { playlist } = this.props;

    return (
      <div className="PlaylistEntry card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h4 className="mb-0">{playlist.name}</h4>

          <div className="btn-group">
            <Link className="btn btn-secondary" to={`/playlists/${playlist.id}`}>Edit</Link>

            <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle.bind(this)}>
              <DropdownToggle caret/>
              <DropdownMenu right>
                <DropdownItem onClick={this.showDeleteModal.bind(this)}>Delete playlist</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </div>

        <div className="card-body">
          <h6>Name: {playlist.name}</h6>
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ dropdownOpen: !prevState.dropdownOpen }));
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

export default connect(mapStateToProps, { showDeleteModal })(PlaylistEntry);
