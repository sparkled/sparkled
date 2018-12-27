import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import withMobileDialog from '@material-ui/core/withMobileDialog/withMobileDialog';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { deleteStage, hideDeleteModal } from '../../actions';

class DeleteStageModal extends Component {

  render() {
    const { deleting, fullScreen, stageToDelete } = this.props;
    const stageName = (stageToDelete || {}).name;

    return (
      <Dialog open={Boolean(stageToDelete)} onClose={this.hideModal} fullScreen={fullScreen}>
        <DialogTitle>Delete stage</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete <em>{stageName}</em>?
          </DialogContentText>
          <DialogContentText>
            If any sequences have been created against this stage, they will also be deleted.
            {this.renderDeletionError()}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={this.props.hideDeleteModal} color="default">
            Cancel
          </Button>
          <Button onClick={this.deleteStage} variant="contained" color="secondary">
            {deleting ? 'Deleting...' : 'Delete stage'}
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
          <div className="card-body">Failed to delete stage: {deleteError}</div>
        </div>
      );
    }
  }

  deleteStage = () => {
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

DeleteStageModal = withMobileDialog({ breakpoint: 'xs' })(DeleteStageModal);
export default connect(mapStateToProps, { deleteStage, hideDeleteModal })(DeleteStageModal);
