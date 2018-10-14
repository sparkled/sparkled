import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import * as sequenceStatuses from '../../sequenceStatuses';
import InputField from '../../../../components/form/InputField';
import SingleSelectField from '../../../../components/form/SingleSelectField';
import { required } from '../../../../components/form/validators';
import { addSequence, hideAddModal } from '../../actions';

const toNumber = value => !value ? null : Number(value);

class AddSequenceModal extends Component {

  componentWillReceiveProps(nextProps) {
    if (!this.props.addModalVisible && nextProps.addModalVisible) {
      this.props.initialize({ framesPerSecond: 60 });
    }
  }

  render() {
    const { adding, addModalVisible, handleSubmit, songs, stages, valid } = this.props;
    const addButtonText = adding ? 'Adding...' : 'Add sequence';

    return (
      <div>
        <Modal isOpen={addModalVisible} wrapClassName="add-sequence-modal" backdrop={true}>
          <form onSubmit={handleSubmit(this.addSequence.bind(this))}>
            <ModalHeader>Add sequence</ModalHeader>
            <ModalBody>
              <Field name="name" component={InputField} label="Sequence Name" type="text"
                     required={true} validate={required}/>

              <Field name="songId" component={SingleSelectField} label="Song" type="text" options={songs}
                     parse={toNumber} required={true} validate={required}/>

              <Field name="stageId" component={SingleSelectField} label="Stage" type="text" options={stages}
                     parse={toNumber} required={true} validate={required}/>

              <Field name="framesPerSecond" component={InputField} label="Frames Per Second" type="number"
                     parse={toNumber} required={true} validate={required}/>

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
          <div className="card-body">Failed to add sequence: {addError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideAddModal();
  }

  addSequence(values) {
    const { addSequence } = this.props;
    const sequence = { ...values, status: sequenceStatuses.NEW };
    addSequence(sequence);
  }
}

function mapStateToProps({ page: { sequenceList } }) {
  return {
    addModalVisible: sequenceList.addModalVisible,
    adding: sequenceList.adding,
    addError: sequenceList.addError
  };
}

AddSequenceModal = connect(mapStateToProps, { addSequence, hideAddModal })(AddSequenceModal);
export default reduxForm({ form: 'addSequence' })(AddSequenceModal);
