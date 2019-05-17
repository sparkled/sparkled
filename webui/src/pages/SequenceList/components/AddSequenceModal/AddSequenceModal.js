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
import SingleSelectField from '../../../../components/form/SingleSelectField';
import TextField from '../../../../components/form/TextField';
import { required } from '../../../../components/form/validators';
import { addSequence, hideAddModal } from '../../actions';

const toNumber = value => !value ? null : Number(value);
const formName = 'addSequence';

class AddSequenceModal extends Component {

  componentWillReceiveProps(nextProps) {
    const { addError, addModalVisible } = nextProps;
    if (!this.props.addModalVisible && addModalVisible) {
      this.props.initialize({ framesPerSecond: 60 });
    }

    if (!this.props.addError && addError) {
      Alert.error(`Failed to add sequence: ${addError}`);
    }
  }

  render() {
    const { adding, addModalVisible, handleSubmit, fullScreen, songs, stages, valid } = this.props;

    return (
      <Dialog open={addModalVisible} onClose={this.props.hideAddModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Add sequence</DialogTitle>
        <DialogContent>
          <form id={formName} onSubmit={handleSubmit(this.addSequence)} noValidate autoComplete="off">
            <Field name="name" component={TextField} fullWidth label="Sequence Name" type="text"
                   required={true} validate={required}/>

            <Field name="songId" component={SingleSelectField} fullWidth label="Song" type="text" options={songs}
                   parse={toNumber} required={true} validate={required}/>

            <Field name="stageId" component={SingleSelectField} fullWidth label="Stage" type="text" options={stages}
                   parse={toNumber} required={true} validate={required}/>

            <Field name="framesPerSecond" component={TextField} fullWidth label="Frames Per Second" type="number"
                   parse={toNumber} required={true} validate={required}/>
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={this.props.hideAddModal} color="default" disabled={adding}>
            Cancel
          </Button>
          <Button variant="contained" color="primary" type="submit" form={formName} disabled={adding || !valid}>
            {adding ? 'Adding...' : 'Add sequence'}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }

  addSequence = sequence => {
    this.props.addSequence(sequence);
  }
}

function mapStateToProps({ page: { sequenceList } }) {
  return {
    addModalVisible: sequenceList.addModalVisible,
    adding: sequenceList.adding,
    addError: sequenceList.addError
  };
}

AddSequenceModal = withMobileDialog({ breakpoint: 'xs' })(AddSequenceModal);
AddSequenceModal = connect(mapStateToProps, { addSequence, hideAddModal })(AddSequenceModal);
export default reduxForm({ form: formName })(AddSequenceModal);
