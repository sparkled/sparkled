import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { deleteSong, hideDeleteModal } from '../../actions';

class DeleteSongModal extends Component {

  render() {
    const { deleting, songToDelete } = this.props;
    const deleteButtonText = deleting ? 'Deleting...' : 'Delete song';

    if (!songToDelete) {
      return null;
    }

    return (
      <div>
        <Modal isOpen={!!songToDelete} wrapClassName='delete-song-modal' backdrop={true}>
          <ModalHeader>Delete song</ModalHeader>
          <ModalBody>
            <p>Are you sure you want to delete <em>{songToDelete.name}</em>?</p>
            <p>If this song has been scheduled for playback, its schedule entries will also be deleted.</p>
            {this.renderDeletionError()}
          </ModalBody>
          <ModalFooter>
            <Button color='danger' disabled={deleting} onClick={this.deleteSong.bind(this)}>{deleteButtonText}</Button>
            <Button color='secondary' disabled={deleting} onClick={this.hideModal.bind(this)}>Cancel</Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  }

  renderDeletionError() {
    const { deleteError } = this.props;
    if (!deleteError) {
      return null;
    } else {
      return (
        <div className='card border-danger'>
          <div className='card-body'>Failed to delete song: {deleteError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideDeleteModal();
  }

  deleteSong() {
    const { deleteSong, songToDelete } = this.props;
    deleteSong(songToDelete.id);
  }
}

function mapStateToProps({ page: { songList } }) {
  return {
    songToDelete: songList.songToDelete,
    deleting: songList.deleting,
    deleteError: songList.deleteError
  };
}

export default connect(mapStateToProps, { deleteSong, hideDeleteModal })(DeleteSongModal);
