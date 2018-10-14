import jsMediaTags from 'jsmediatags/dist/jsmediatags';
import React, { Component } from 'react';
import Dropzone from 'react-dropzone';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import InputField from '../../../../components/form/InputField';
import { required } from '../../../../components/form/validators';
import { addSong, hideAddModal } from '../../actions';
import './AddSongModal.css';

class AddSongModal extends Component {

  state = { mp3: null };

  componentWillReceiveProps(nextProps) {
    if (!this.props.addModalVisible && nextProps.addModalVisible) {
      this.props.initialize({});
      this.setState({mp3: null});
    }
  }

  render() {
    const { adding, addModalVisible, handleSubmit, valid } = this.props;
    const addButtonText = adding ? 'Adding...' : 'Add song';
    const canSubmit = valid && this.state.mp3;
    const dropzoneText = this.state.mp3 ? `Selected file: ${this.state.mp3.name}.` : 'Drop .mp3 file here, or click.';

    return (
      <div>
        <Modal isOpen={addModalVisible} wrapClassName="AddSongModal" backdrop={true}>
          <form onSubmit={handleSubmit(this.addSong.bind(this))}>
            <ModalHeader>Add song</ModalHeader>
            <ModalBody>
              <Dropzone className="drop-zone mb-4" acceptClassName="drop-zone-accept" rejectClassName="drop-zone-reject"
                        accept=".mp3" multiple={false}
                        onDrop={this.onDrop.bind(this)}>
                {dropzoneText}
              </Dropzone>

              <Field name="name" component={InputField} label="Song Name" type="text"
                     required={true} validate={required}/>

              <div className="row">
                <div className="col-sm-6">
                  <Field name="artist" component={InputField} label="Artist" type="text"
                         required={true} validate={required}/>
                </div>
                <div className="col-sm-6">
                  <Field name="album" component={InputField} label="Album" type="text"
                         required={true} validate={required}/>
                </div>
              </div>

              <div className="d-none">
                <Field name="durationMs" component={InputField} label="Duration (ms)" type="number" disabled={true}/>
              </div>

              {this.renderAddError()}
            </ModalBody>
            <ModalFooter>
              <Button type="submit" color="info" disabled={adding || !canSubmit}>{addButtonText}</Button>
              <Button type="button" color="secondary" disabled={adding}
                      onClick={this.hideModal.bind(this)}>Cancel</Button>
            </ModalFooter>
          </form>
        </Modal>
      </div>
    );
  }

  onDrop(acceptedFiles, rejectedFiles) {
    if (acceptedFiles.length > 0) {
      this.setState({ mp3: acceptedFiles[0] });
      jsMediaTags.read(acceptedFiles[0], {
        onSuccess: tag => this.populateSongFields(tag.tags),
        onError: error => this.populateSongFields({})
      });
    }
  }

  populateSongFields(tags) {
    const { change } = this.props;
    const { title, artist, album } = tags;

    change('durationMs', null);

    const audio = document.createElement('audio');
    audio.src = this.state.mp3.preview;
    audio.addEventListener('canplaythrough', () => {
      change('durationMs', Math.ceil(audio.duration * 1000));
    });

    change('name', title);
    change('artist', artist);
    change('album', album);
  }

  renderAddError() {
    const { addError } = this.props;
    if (!addError) {
      return null;
    } else {
      return (
        <div className="card border-danger">
          <div className="card-body">Failed to add song: {addError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideAddModal();
  }

  addSong(values) {
    const { addSong } = this.props;
    const { mp3 } = this.state;
    addSong({ song: values, mp3 });
  }
}

function mapStateToProps({ page: { songList } }) {
  return {
    addModalVisible: songList.addModalVisible,
    adding: songList.adding,
    addError: songList.addError
  };
}

AddSongModal = connect(mapStateToProps, { addSong, hideAddModal })(AddSongModal);
export default reduxForm({ form: 'addSong' })(AddSongModal);
