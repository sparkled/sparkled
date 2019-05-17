import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import withMobileDialog from '@material-ui/core/withMobileDialog/withMobileDialog';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { deleteScheduledJob, hideDeleteModal } from '../../actions';

class DeleteScheduledJobModal extends Component {

  render() {
    const { deleting, fullScreen, scheduledJobToDelete } = this.props;

    return (
      <Dialog open={Boolean(scheduledJobToDelete)} onClose={this.hideModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Delete scheduled job</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete this scheduled job?
            {this.renderDeletionError()}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={this.props.hideDeleteModal} color="default">
            Cancel
          </Button>
          <Button onClick={this.deleteScheduledJob} variant="contained" color="secondary">
            {deleting ? 'Deleting...' : 'Delete scheduledJob'}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }

  renderDeletionError() {
    const { deleteError } = this.props;
    if (!deleteError) {
      return null;
    } else {
      return (
        <div className="card border-danger">
          <div className="card-body">Failed to delete scheduledJob: {deleteError}</div>
        </div>
      );
    }
  }

  deleteScheduledJob = () => {
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

DeleteScheduledJobModal = withMobileDialog({ breakpoint: 'xs' })(DeleteScheduledJobModal);
export default connect(mapStateToProps, { deleteScheduledJob, hideDeleteModal })(DeleteScheduledJobModal);
