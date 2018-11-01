import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import jobActions from '../../jobActions';
import InputField from '../../../../components/form/InputField';
import SingleSelectField from '../../../../components/form/SingleSelectField';
import { required } from '../../../../components/form/validators';
import { addScheduledJob, hideAddModal } from '../../actions';

class AddScheduledJobModal extends Component {

  state = {
    action: null
  }

  constructor(props) {
    super(props);
    this.onActionChange = this.onActionChange.bind(this);
  }

  render() {
    const { adding, addModalVisible, handleSubmit, valid } = this.props;
    const addButtonText = adding ? 'Adding...' : 'Add scheduled job';

    return (
      <div>
        <Modal isOpen={addModalVisible} wrapClassName="AddScheduledJobModal" backdrop={true}>
          <form onSubmit={handleSubmit(this.addScheduledJob.bind(this))}>
            <ModalHeader>Add scheduled job</ModalHeader>
            <ModalBody>
              <Field name="cronExpression" component={InputField} label="Cron Expression" type="text"
                     required={true} validate={required}/>

              <Field name="action" component={SingleSelectField} label="Action" options={jobActions}
                     required={true} validate={required} onChange={this.onActionChange}/>

              {this.renderValueField()}
              {this.renderPlaylistField()}
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

  onActionChange(event, newValue) {
    const { change } = this.props;
    change('value', null);
    change('playlistId', null);
    this.setState({ action: newValue });
  }

  renderPlaylistField() {
    const hasPlaylist = this.state.action === 'PLAY_PLAYLIST';

    if (hasPlaylist) {
      return (
        <Field name="playlistId" component={SingleSelectField} label="Playlist" options={this.props.playlists}
               required={true} validate={required}/>
      );
    } else {
      return null;
    }
  }

  renderValueField() {
    const hasValue = this.state.action === 'SET_BRIGHTNESS';

    if (hasValue) {
      return (
        <Field name="value" component={InputField} label="Value" type="text"
               required={true} validate={required}/>
      );
    } else {
      return null;
    }
  }

  renderAddError() {
    const { addError } = this.props;
    if (!addError) {
      return null;
    } else {
      return (
        <div className="card border-danger">
          <div className="card-body">Failed to add scheduled job: {addError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideAddModal();
  }

  addScheduledJob(values) {
    this.props.addScheduledJob(values);
  }
}

function mapStateToProps({ page: { scheduler } }) {
  return {
    addModalVisible: scheduler.addModalVisible,
    adding: scheduler.adding,
    addError: scheduler.addError
  };
}

AddScheduledJobModal = connect(mapStateToProps, { addScheduledJob, hideAddModal })(AddScheduledJobModal);
export default reduxForm({ form: 'addScheduledJob' })(AddScheduledJobModal);
