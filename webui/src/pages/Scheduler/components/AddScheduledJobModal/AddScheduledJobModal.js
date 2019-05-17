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
import SingleSelectField from '../../../../components/form/SingleSelectField';
import { required } from '../../../../components/form/validators';
import { addScheduledJob, hideAddModal } from '../../actions';
import jobActions from '../../jobActions';

const formName = 'addScheduledJob';

class AddScheduledJobModal extends Component {

  state = {
    action: null
  }

  componentWillReceiveProps(nextProps) {
    const { addError, addModalVisible } = nextProps;
    if (!this.props.addModalVisible && addModalVisible) {
      this.props.initialize({});
    }

    if (!this.props.addError && addError) {
      Alert.error(`Failed to add scheduled job: ${addError}`);
    }
  }

  render() {
    const { adding, addModalVisible, handleSubmit, fullScreen, valid } = this.props;

    return (
      <Dialog open={addModalVisible} onClose={this.props.hideAddModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Add scheduled job</DialogTitle>
        <DialogContent>
          <form id={formName} onSubmit={handleSubmit(this.addScheduledJob)} noValidate autoComplete="off">
            <Field name="cronExpression" component={TextField} fullWidth label="Cron Expression" type="text"
                   required={true} validate={required}/>

            <Field name="action" component={SingleSelectField} fullWidth label="Action" options={jobActions}
                   required={true} validate={required} onChange={this.onActionChange}/>

            {this.renderValueField()}
            {this.renderPlaylistField()}
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={this.props.hideAddModal} color="default" disabled={adding}>
          Cancel
          </Button>
          <Button variant="contained" color="primary" type="submit" form={formName} disabled={adding || !valid}>
            {adding ? 'Adding...' : 'Add scheduled job'}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }

  onActionChange = (event, newValue) => {
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
        <Field name="value" component={TextField} fullWidth label="Brightness" required={true} validate={required}/>
      );
    } else {
      return null;
    }
  }

  addScheduledJob = scheduledJob => {
    this.props.addScheduledJob(scheduledJob);
  }
}

function mapStateToProps({ page: { scheduler } }) {
  return {
    addModalVisible: scheduler.addModalVisible,
    adding: scheduler.adding,
    addError: scheduler.addError
  };
}

AddScheduledJobModal = withMobileDialog({ breakpoint: 'xs' })(AddScheduledJobModal);
AddScheduledJobModal = connect(mapStateToProps, { addScheduledJob, hideAddModal })(AddScheduledJobModal);
export default reduxForm({ form: 'addScheduledJob' })(AddScheduledJobModal);
