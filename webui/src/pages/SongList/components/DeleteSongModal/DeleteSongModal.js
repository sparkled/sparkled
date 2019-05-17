import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import withMobileDialog from '@material-ui/core/withMobileDialog/withMobileDialog';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { deleteSong, hideDeleteModal } from '../../actions';

class DeleteSongModal extends Component {

  render() {
    const { deleting, fullScreen, songToDelete } = this.props;
    const songName = (songToDelete || {}).name;

    return (
      <Dialog open={Boolean(songToDelete)} onClose={this.hideModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Delete song</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete <em>{songName}</em>?
          </DialogContentText>
          <DialogContentText>
            If any sequences have been created against this song, they will also be deleted.
            {this.renderDeletionError()}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={this.props.hideDeleteModal} color="default">
            Cancel
          </Button>
          <Button onClick={this.deleteSong} variant="contained" color="secondary">
            {deleting ? 'Deleting...' : 'Delete song'}
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
          <div className="card-body">Failed to delete song: {deleteError}</div>
        </div>
      );
    }
  }

  deleteSong = () => {
    const { deleteSong, songToDelete } = this.props;
    deleteSong(songToDelete.id);
  }
}

function mapStateToProps({ page: { songList } }) {
  return {
    songToDelete: songList.songToDelete,
    deleting: songList.deleting,
    deleteError: songList.deleteError
  };
}

DeleteSongModal = withMobileDialog({ breakpoint: 'xs' })(DeleteSongModal);
export default connect(mapStateToProps, { deleteSong, hideDeleteModal })(DeleteSongModal);
