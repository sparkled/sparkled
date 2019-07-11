import { Button, Dialog, DialogActions, DialogContent, DialogTitle, withMobileDialog } from '@material-ui/core';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Field, reduxForm } from 'redux-form';
import TextField from '../../../../components/form/TextField';
import { required } from '../../../../components/form/validators';
import { addStage, hideAddModal } from '../../actions';

const formName = 'addStage';

class AddStageModal extends Component {

  componentWillReceiveProps(nextProps) {
    const { addError, addModalVisible } = nextProps;
    if (!this.props.addModalVisible && addModalVisible) {
      this.props.initialize({});
    }

    if (!this.props.addError && addError) {
      Alert.error(`Failed to add stage: ${addError}`);
    }
  }

  render() {
    const { adding, addModalVisible, handleSubmit, fullScreen, valid } = this.props;

    return (
      <Dialog open={addModalVisible} onClose={this.props.hideAddModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Add stage</DialogTitle>
        <DialogContent>
          <form id={formName} onSubmit={handleSubmit(this.addStage)} noValidate autoComplete="off">
            <Field component={TextField} fullWidth name="name" label="Stage Name"
                   required={true} validate={required}/>
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={this.props.hideAddModal} color="default" disabled={adding}>
            Cancel
          </Button>
          <Button variant="contained" color="primary" type="submit" form={formName} disabled={adding || !valid}>
            {adding ? 'Adding...' : 'Add stage'}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }

  addStage = stage => {
    this.props.addStage({ ...stage, width: 800, height: 600 });
  }
}

function mapStateToProps({ page: { stageList } }) {
  return {
    addModalVisible: stageList.addModalVisible,
    adding: stageList.adding,
    addError: stageList.addError
  };
}

AddStageModal = withMobileDialog({ breakpoint: 'xs' })(AddStageModal);
AddStageModal = connect(mapStateToProps, { addStage, hideAddModal })(AddStageModal);
export default reduxForm({ form: formName })(AddStageModal);
