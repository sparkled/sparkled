import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import InputField from '../../../../components/form/InputField';
import { required } from '../../../../components/form/validators';
import { addStage, hideAddModal } from '../../actions';

class AddStageModal extends Component {

  componentWillReceiveProps(nextProps) {
    if (!this.props.addModalVisible && nextProps.addModalVisible) {
      this.props.initialize({});
    }
  }

  render() {
    const { adding, addModalVisible, handleSubmit, valid } = this.props;
    const addButtonText = adding ? 'Adding...' : 'Add stage';
    const canSubmit = valid;

    return (
      <div>
        <Modal isOpen={addModalVisible} wrapClassName="add-stage-modal" backdrop={true}>
          <form onSubmit={handleSubmit(this.addStage.bind(this))}>
            <ModalHeader>Add stage</ModalHeader>
            <ModalBody>

              <Field name="name" component={InputField} label="Stage Name" type="text"
                     required={true} validate={required}/>

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

  renderAddError() {
    const { addError } = this.props;
    if (!addError) {
      return null;
    } else {
      return (
        <div className="card border-danger">
          <div className="card-body">Failed to add stage: {addError}</div>
        </div>
      );
    }
  }

  hideModal() {
    this.props.hideAddModal();
  }

  addStage(stage) {
    const { addStage } = this.props;
    addStage({ ...stage, width: 800, height: 600 });
  }
}

function mapStateToProps({ page: { stageList } }) {
  return {
    addModalVisible: stageList.addModalVisible,
    adding: stageList.adding,
    addError: stageList.addError
  };
}

AddStageModal = connect(mapStateToProps, { addStage, hideAddModal })(AddStageModal);
export default reduxForm({ form: 'addStage' })(AddStageModal);
