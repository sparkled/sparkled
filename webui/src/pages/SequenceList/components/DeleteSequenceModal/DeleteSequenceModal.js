import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@material-ui/core';
import withMobileDialog from '@material-ui/core/withMobileDialog/withMobileDialog';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { deleteSequence, hideDeleteModal } from '../../actions';

class DeleteSequenceModal extends Component {

  render() {
    const { deleting, fullScreen, sequenceToDelete } = this.props;
    const sequenceName = (sequenceToDelete || {}).name;

    return (
      <Dialog open={Boolean(sequenceToDelete)} onClose={this.hideModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Delete sequence</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete <em>{sequenceName}</em>?
            {this.renderDeletionError()}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={this.props.hideDeleteModal} color="default">
            Cancel
          </Button>
          <Button onClick={this.deleteSequence} variant="contained" color="secondary">
            {deleting ? 'Deleting...' : 'Delete sequence'}
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
          <div className="card-body">Failed to delete sequence: {deleteError}</div>
        </div>
      );
    }
  }

  deleteSequence = () => {
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

DeleteSequenceModal = withMobileDialog({ breakpoint: 'xs' })(DeleteSequenceModal);
export default connect(mapStateToProps, { deleteSequence, hideDeleteModal })(DeleteSequenceModal);
