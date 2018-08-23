import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { deleteStage, hideDeleteModal } from '../../actions';

class DeleteStageModal extends Component {

  render() {
    const { deleting, stageToDelete } = this.props;
    const deleteButtonText = deleting ? 'Deleting...' : 'Delete stage';

    if (!stageToDelete) {
      return null;
    }

    return (
      <div>
        <Modal isOpen={!!stageToDelete} wrapClassName='delete-stage-modal' backdrop={true}>
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
        <div className='card border-danger'>
          <div className='card-body'>Failed to delete stage: {deleteError}</div>
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

function mapStateToProps({ page: { stageList } }) {
  return {
    stageToDelete: stageList.stageToDelete,
    deleting: stageList.deleting,
    deleteError: stageList.deleteError
  };
}

export default connect(mapStateToProps, { deleteStage, hideDeleteModal })(DeleteStageModal);
