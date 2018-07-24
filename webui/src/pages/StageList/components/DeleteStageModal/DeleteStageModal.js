import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { deleteStage } from '../../../../services/stage/actions';
import { hideDeleteModal } from '../../actions';

class DeleteStageModal extends Component {

  render() {
    const { deleting, stageToDelete } = this.props;
    const deleteButtonText = deleting ? 'Deleting...' : 'Delete stage';

    // TODO: Workaround for isOpen not removing the modal on completion. Try removing once reactstrap v5 is released.
    if (!stageToDelete) {
      return null;
    }

    // TODO: Remove autoFocus, modalClassName and backdropClassName from Modal below once reactstrap v5 is released.
    return (
      <div>
        <Modal isOpen={!!stageToDelete} wrapClassName='delete-stage-modal' modalClassName='show' backdropClassName='show'
               backdrop={true} autoFocus={false}>
          <ModalHeader>Delete stage</ModalHeader>
          <ModalBody>
            <p>Are you sure you want to delete <em>{stageToDelete.name}</em>?</p>
            <p>If any songs have been created against this stage, they will also be deleted.</p>
            {this.renderDeletionError()}
          </ModalBody>
          <ModalFooter>
            <Button color='danger' disabled={deleting} onClick={this.deleteStage.bind(this)}>{deleteButtonText}</Button>
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
          <div className='card-block'>Failed to delete stage: {deleteError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideDeleteModal();
  }

  deleteStage() {
    const { deleteStage, stageToDelete } = this.props;
    deleteStage(stageToDelete.id);
  }
}

function mapStateToProps({ data: { stages }, page: { stageList } }) {
  return {
    stageToDelete: stageList.stageToDelete,
    deleting: stages.deleting,
    deleteError: stages.deleteError
  };
}

export default connect(mapStateToProps, { deleteStage, hideDeleteModal })(DeleteStageModal);
