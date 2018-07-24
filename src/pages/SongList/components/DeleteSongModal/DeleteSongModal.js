import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { deleteSong } from '../../../../services/song/actions';
import { hideDeleteModal } from '../../actions';

class DeleteSongModal extends Component {

  render() {
    const { deleting, songToDelete } = this.props;
    const deleteButtonText = deleting ? 'Deleting...' : 'Delete song';

    // TODO: Workaround for isOpen not removing the modal on completion. Try removing once reactstrap v5 is released.
    if (!songToDelete) {
      return null;
    }

    // TODO: Remove autoFocus, modalClassName and backdropClassName from Modal below once reactstrap v5 is released.
    return (
      <div>
        <Modal isOpen={!!songToDelete} wrapClassName='delete-song-modal' modalClassName='show' backdropClassName='show'
               backdrop={true} autoFocus={false}>
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
        <div className='card card-outline-danger'>
          <div className='card-block'>Failed to delete song: {deleteError}</div>
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

function mapStateToProps({ data: { songs }, page: { songList } }) {
  return {
    songToDelete: songList.songToDelete,
    deleting: songs.deleting,
    deleteError: songs.deleteError
  };
}

export default connect(mapStateToProps, { deleteSong, hideDeleteModal })(DeleteSongModal);
