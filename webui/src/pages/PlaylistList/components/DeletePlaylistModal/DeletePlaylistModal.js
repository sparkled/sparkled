import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import withMobileDialog from '@material-ui/core/withMobileDialog/withMobileDialog';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { deletePlaylist, hideDeleteModal } from '../../actions';

class DeletePlaylistModal extends Component {

  render() {
    const { deleting, fullScreen, playlistToDelete } = this.props;
    const playlistName = (playlistToDelete || {}).name;

    return (
      <Dialog open={Boolean(playlistToDelete)} onClose={this.hideModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Delete playlist</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete <em>{playlistName}</em>?
            {this.renderDeletionError()}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={this.props.hideDeleteModal} color="default">
            Cancel
          </Button>
          <Button onClick={this.deletePlaylist} variant="contained" color="secondary">
            {deleting ? 'Deleting...' : 'Delete playlist'}
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
          <div className="card-body">Failed to delete playlist: {deleteError}</div>
        </div>
      );
    }
  }

  deletePlaylist = () => {
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

DeletePlaylistModal = withMobileDialog({ breakpoint: 'xs' })(DeletePlaylistModal);
export default connect(mapStateToProps, { deletePlaylist, hideDeleteModal })(DeletePlaylistModal);
