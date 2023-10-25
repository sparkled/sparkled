import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Field, reduxForm } from 'redux-form'
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import SingleSelectField from '../../../../components/form/SingleSelectField'
import { required } from '../../../../components/form/validators'
import { addSequence, hideAddSequenceModal } from '../../actions'
import { uniqueId } from '../../../../utils/idUtils'

class AddSequenceModal extends Component {
  componentWillReceiveProps(nextProps) {
    const { visible } = this.props
    if (!visible && nextProps.visible) {
      this.props.initialize({})
    }
  }

  render() {
    const { sequences, visible, handleSubmit, valid } = this.props

    return (
      <div>
        <Modal isOpen={visible} wrapClassName='AddSequenceModal' backdrop>
          <form onSubmit={handleSubmit(this.addSequence.bind(this))}>
            <ModalHeader>Add channel</ModalHeader>
            <ModalBody>
              <Field
                name='sequenceId'
                component={SingleSelectField}
                options={sequences}
                label='Sequence'
                required
                validate={required}
              />
            </ModalBody>
            <ModalFooter>
              <Button type='submit' color='info' disabled={!valid}>
                Add sequence
              </Button>
              <Button type='button' color='secondary' onClick={this.hideModal.bind(this)}>
                Cancel
              </Button>
            </ModalFooter>
          </form>
        </Modal>
      </div>
    )
  }

  addSequence(playlistSequence) {
    const { playlist } = this.props
    const displayOrder = playlist.sequences.length
    this.props.addSequence({
      ...playlistSequence,
      id: uniqueId(),
      playlistId: playlist.id,
      displayOrder,
    })
  }

  hideModal() {
    this.props.hideAddSequenceModal()
  }
}

function mapStateToProps(state) {
  const { playlistEdit } = state.page

  return {
    playlist: playlistEdit.present.playlist,
    sequences: playlistEdit.present.sequences,
    visible: playlistEdit.present.addSequenceModalVisible,
  }
}

AddSequenceModal = connect(mapStateToProps, { addSequence, hideAddSequenceModal })(AddSequenceModal)
export default reduxForm({ form: 'addSequence' })(AddSequenceModal)
