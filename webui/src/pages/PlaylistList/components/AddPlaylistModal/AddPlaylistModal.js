import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import withMobileDialog from '@material-ui/core/withMobileDialog';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Field, reduxForm } from 'redux-form';
import TextField from '../../../../components/form/TextField';
import { required } from '../../../../components/form/validators';
import { addPlaylist, hideAddModal } from '../../actions';

const formName = 'addPlaylist';

class AddPlaylistModal extends Component {

  componentWillReceiveProps(nextProps) {
    const { addError, addModalVisible } = nextProps;
    if (!this.props.addModalVisible && addModalVisible) {
      this.props.initialize({});
    }

    if (!this.props.addError && addError) {
      Alert.error(`Failed to add playlist: ${addError}`);
    }
  }

  render() {
    const { adding, addModalVisible, handleSubmit, fullScreen, valid } = this.props;

    return (
      <Dialog open={addModalVisible} onClose={this.props.hideAddModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Add playlist</DialogTitle>
        <DialogContent>
          <form id={formName} onSubmit={handleSubmit(this.addPlaylist)} noValidate autoComplete="off">
            <Field component={TextField} fullWidth name="name" label="Playlist Name"
                   required={true} validate={required}/>
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={this.props.hideAddModal} color="default" disabled={adding}>
            Cancel
          </Button>
          <Button variant="contained" color="primary" type="submit" form={formName} disabled={adding || !valid}>
            {adding ? 'Adding...' : 'Add playlist'}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }

  addPlaylist = playlist => {
    this.props.addPlaylist(playlist);
  }
}

function mapStateToProps({ page: { playlistList } }) {
  return {
    addModalVisible: playlistList.addModalVisible,
    adding: playlistList.adding,
    addError: playlistList.addError
  };
}

AddPlaylistModal = withMobileDialog({ breakpoint: 'xs' })(AddPlaylistModal);
AddPlaylistModal = connect(mapStateToProps, { addPlaylist, hideAddModal })(AddPlaylistModal);
export default reduxForm({ form: formName })(AddPlaylistModal);
