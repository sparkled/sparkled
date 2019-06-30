import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { withStyles } from '@material-ui/core/styles';
import withMobileDialog from '@material-ui/core/withMobileDialog';
import jsMediaTags from 'jsmediatags/dist/jsmediatags';
import React, { Component } from 'react';
import Dropzone from 'react-dropzone';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Field, reduxForm } from 'redux-form';
import TextField from '../../../../components/form/TextField';
import { required } from '../../../../components/form/validators';
import { addSong, hideAddModal } from '../../actions';

const formName = 'addSong';

const styles = theme => ({
  dropZone: {
    marginBottom: 24,
    padding: 24,
    borderRadius: theme.shape.borderRadius,
    background: theme.palette.grey[900],
    fontFamily: theme.typography.fontFamily,
    '&:hover, &.drop-zone-accept, &.drop-zone-reject': {
      background: theme.palette.grey[700],
      cursor: 'pointer'
    }
  }
});

class AddSongModal extends Component {

  state = { mp3: null };

  componentWillReceiveProps(nextProps) {
    const { addError, addModalVisible } = nextProps;

    if (!this.props.addModalVisible && addModalVisible) {
      this.props.initialize({});
      this.setState({ mp3: null });
    }

    if (!this.props.addError && addError) {
      Alert.error(`Failed to add song: ${addError}`);
    }
  }

  render() {
    const { adding, addModalVisible, classes, handleSubmit, fullScreen, valid } = this.props;
    const canSubmit = valid && this.state.mp3;
    const dropzoneText = this.state.mp3 ? `Selected file: ${this.state.mp3.name}.` : 'Drop .mp3 file here, or click.';

    return (
      <Dialog open={addModalVisible} onClose={this.props.hideAddModal} fullScreen={fullScreen} fullWidth>
        <DialogTitle>Add song</DialogTitle>
        <DialogContent>
          <form id={formName} onSubmit={handleSubmit(this.addSong)} noValidate autoComplete="off">
            <Dropzone className={classes.dropZone} acceptClassName="drop-zone-accept" rejectClassName="drop-zone-reject"
                      accept=".mp3" multiple={false}
                      onDrop={this.onDrop.bind(this)}>
              {dropzoneText}
            </Dropzone>

            <Field component={TextField} fullWidth name="name" label="Song Name"
                   required={true} validate={required}/>

            <Field component={TextField} fullWidth name="artist" label="Artist"
                   required={true} validate={required}/>

            <Field component={TextField} fullWidth name="album" label="Album"
                   required={true} validate={required}/>

            <div className="d-none">
              <Field name="durationMs" component={TextField} label="Duration (ms)" type="number" disabled={true}/>
            </div>
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={this.props.hideAddModal} color="default" disabled={adding}>
            Cancel
          </Button>
          <Button variant="contained" color="primary" type="submit" form={formName} disabled={adding || !canSubmit}>
            {adding ? 'Adding...' : 'Add song'}
          </Button>
        </DialogActions>
      </Dialog>
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

  addSong = song => {
    const { mp3 } = this.state;
    this.props.addSong({ song, mp3 });
  }
}

function mapStateToProps({ page: { songList } }) {
  return {
    addModalVisible: songList.addModalVisible,
    adding: songList.adding,
    addError: songList.addError
  };
}

AddSongModal = withMobileDialog({ breakpoint: 'xs' })(AddSongModal);
AddSongModal = connect(mapStateToProps, { addSong, hideAddModal })(AddSongModal);
AddSongModal = reduxForm({ form: formName })(AddSongModal);
export default withStyles(styles)(AddSongModal);
