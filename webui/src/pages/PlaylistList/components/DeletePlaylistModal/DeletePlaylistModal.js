import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { deletePlaylist, hideDeleteModal } from '../../actions';

class DeletePlaylistModal extends Component {

  render() {
    const { deleting, playlistToDelete } = this.props;
    const deleteButtonText = deleting ? 'Deleting...' : 'Delete playlist';

    if (!playlistToDelete) {
      return null;
    }

    return (
      <div>
        <Modal isOpen={!!playlistToDelete} wrapClassName='delete-playlist-modal' backdrop={true}>
          <ModalHeader>Delete playlist</ModalHeader>
          <ModalBody>
            <p>Are you sure you want to delete <em>{playlistToDelete.name}</em>?</p>
            {this.renderDeletionError()}
          </ModalBody>
          <ModalFooter>
            <Button color='danger' disabled={deleting} onClick={this.deletePlaylist.bind(this)}>{deleteButtonText}</Button>
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
          <div className='card-body'>Failed to delete playlist: {deleteError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideDeleteModal();
  }

  deletePlaylist() {
    const { deletePlaylist, playlistToDelete } = this.props;
    deletePlaylist(playlistToDelete.id);
  }
}

function mapStateToProps({ page: { playlistList } }) {
  return {
    playlistToDelete: playlistList.playlistToDelete,
    deleting: playlistList.deleting,
    deleteError: playlistList.deleteError
  };
}

export default connect(mapStateToProps, { deletePlaylist, hideDeleteModal })(DeletePlaylistModal);
