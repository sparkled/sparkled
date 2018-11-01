import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { deleteScheduledJob, hideDeleteModal } from '../../actions';

class DeleteScheduledJobModal extends Component {

  render() {
    const { deleting, scheduledJobToDelete } = this.props;
    const deleteButtonText = deleting ? 'Deleting...' : 'Delete scheduled job';

    if (!scheduledJobToDelete) {
      return null;
    }

    return (
      <div>
        <Modal isOpen={!!scheduledJobToDelete} wrapClassName="DeleteScheduledJobModal" backdrop={true}>
          <ModalHeader>Delete scheduled job</ModalHeader>
          <ModalBody>
            <p>Are you sure you want to delete this scheduled job?</p>
            {this.renderDeletionError()}
          </ModalBody>
          <ModalFooter>
            <Button color="danger" disabled={deleting} onClick={this.deleteScheduledJob.bind(this)}>{deleteButtonText}</Button>
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
          <div className="card-body">Failed to delete scheduled job: {deleteError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideDeleteModal();
  }

  deleteScheduledJob() {
    const { deleteScheduledJob, scheduledJobToDelete } = this.props;
    deleteScheduledJob(scheduledJobToDelete.id);
  }
}

function mapStateToProps({ page: { scheduler } }) {
  return {
    scheduledJobToDelete: scheduler.scheduledJobToDelete,
    deleting: scheduler.deleting,
    deleteError: scheduler.deleteError
  };
}

export default connect(mapStateToProps, { deleteScheduledJob, hideDeleteModal })(DeleteScheduledJobModal);
