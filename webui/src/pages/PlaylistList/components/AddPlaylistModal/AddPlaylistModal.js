import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import InputField from '../../../../components/form/InputField';
import { required } from '../../../../components/form/validators';
import { addPlaylist, hideAddModal } from '../../actions';

class AddPlaylistModal extends Component {

  render() {
    const { adding, addModalVisible, handleSubmit, valid } = this.props;
    const addButtonText = adding ? 'Adding...' : 'Add playlist';

    return (
      <div>
        <Modal isOpen={addModalVisible} wrapClassName="add-playlist-modal" backdrop={true}>
          <form onSubmit={handleSubmit(this.addPlaylist.bind(this))}>
            <ModalHeader>Add playlist</ModalHeader>
            <ModalBody>
              <Field name="name" component={InputField} label="Playlist Name" type="text"
                     required={true} validate={required}/>
              {this.renderAddError()}
            </ModalBody>
            <ModalFooter>
              <Button type="submit" color="info" disabled={adding || !valid}>{addButtonText}</Button>
              <Button type="button" color="secondary" disabled={adding}
                      onClick={this.hideModal.bind(this)}>Cancel</Button>
            </ModalFooter>
          </form>
        </Modal>
      </div>
    );
  }

  renderAddError() {
    const { addError } = this.props;
    if (!addError) {
      return null;
    } else {
      return (
        <div className="card border-danger">
          <div className="card-body">Failed to add playlist: {addError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideAddModal();
  }

  addPlaylist(values) {
    this.props.addPlaylist(values);
  }
}

function mapStateToProps({ page: { playlistList } }) {
  return {
    addModalVisible: playlistList.addModalVisible,
    adding: playlistList.adding,
    addError: playlistList.addError
  };
}

AddPlaylistModal = connect(mapStateToProps, { addPlaylist, hideAddModal })(AddPlaylistModal);
export default reduxForm({ form: 'addPlaylist' })(AddPlaylistModal);
