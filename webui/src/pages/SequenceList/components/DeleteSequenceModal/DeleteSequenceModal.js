import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { deleteSequence, hideDeleteModal } from '../../actions';

class DeleteSequenceModal extends Component {

  render() {
    const { deleting, sequenceToDelete } = this.props;
    const deleteButtonText = deleting ? 'Deleting...' : 'Delete sequence';

    if (!sequenceToDelete) {
      return null;
    }

    return (
      <div>
        <Modal isOpen={!!sequenceToDelete} wrapClassName="delete-sequence-modal" backdrop={true}>
          <ModalHeader>Delete sequence</ModalHeader>
          <ModalBody>
            <p>Are you sure you want to delete <em>{sequenceToDelete.name}</em>?</p>
            {this.renderDeletionError()}
          </ModalBody>
          <ModalFooter>
            <Button color="danger" disabled={deleting} onClick={this.deleteSequence.bind(this)}>{deleteButtonText}</Button>
            <Button color="secondary" disabled={deleting} onClick={this.hideModal.bind(this)}>Cancel</Button>
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
        <div className="card border-danger">
          <div className="card-body">Failed to delete sequence: {deleteError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideDeleteModal();
  }

  deleteSequence() {
    const { deleteSequence, sequenceToDelete } = this.props;
    deleteSequence(sequenceToDelete.id);
  }
}

function mapStateToProps({ page: { sequenceList } }) {
  return {
    sequenceToDelete: sequenceList.sequenceToDelete,
    deleting: sequenceList.deleting,
    deleteError: sequenceList.deleteError
  };
}

export default connect(mapStateToProps, { deleteSequence, hideDeleteModal })(DeleteSequenceModal);
