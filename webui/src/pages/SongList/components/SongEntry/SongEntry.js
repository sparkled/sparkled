import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { getFormattedDuration } from '../../../../utils/dateUtils';
import { showDeleteModal } from '../../actions';
import './SongEntry.css';

class SongEntry extends Component {

  state = { dropdownOpen: false };

  render() {
    const { song } = this.props;

    return (
      <div className="SongEntry card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h4 className="mb-0">{song.name}</h4>

          <div className="btn-group">
            <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle.bind(this)}>
              <DropdownToggle caret/>
              <DropdownMenu right>
                <DropdownItem onClick={this.showDeleteModal.bind(this)}>Delete Song</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </div>

        <div className="card-body">
          <div className="d-flex justify-content-between">
            <h6>Album: {song.album || 'No album'}</h6>
            <h6 className="ml-3">{getFormattedDuration(Math.floor(song.durationMs / 1000))}</h6>
          </div>

          <div className="d-flex justify-content-between">
            <h6>Artist: {song.artist || 'No artist'}</h6>
          </div>
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ dropdownOpen: !prevState.dropdownOpen }));
  }

  showDeleteModal() {
    const { showDeleteModal, song } = this.props;
    showDeleteModal(song);
  }
}

function mapStateToProps({ page: { songList } }) {
  return {
    songToDelete: songList.songToDelete,
    deleting: songList.deleting
  };
}

export default connect(mapStateToProps, { showDeleteModal })(SongEntry);
