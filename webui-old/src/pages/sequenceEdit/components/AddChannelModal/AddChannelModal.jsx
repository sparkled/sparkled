import _ from 'lodash'
import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Field, formValueSelector, reduxForm } from 'redux-form'
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import InputField from '../../../../components/form/InputField'
import SingleSelectField from '../../../../components/form/SingleSelectField'
import { required } from '../../../../components/form/validators'
import { addChannel, hideAddChannelModal } from '../../actions'
import { uniqueId } from '../../../../utils/idUtils'

const FORM_NAME = 'addChannel'

class AddChannelModal extends Component {
  componentWillReceiveProps(nextProps) {
    const { visible, selectedStagePropUuid, stageProps, change } = this.props
    if (!visible && nextProps.visible) {
      this.props.initialize({})
    }

    if (selectedStagePropUuid !== nextProps.selectedStagePropUuid) {
      const stageProp = _.find(stageProps, {
        id: nextProps.selectedStagePropUuid,
      })
      const channelName = stageProp ? stageProp.name : ''
      change('name', channelName)
    }
  }

  render() {
    const { stageProps, visible, handleSubmit, valid } = this.props

    return (
      <div>
        <Modal isOpen={visible} wrapClassName='add-channel-modal' backdrop>
          <form onSubmit={handleSubmit(this.addChannel.bind(this))}>
            <ModalHeader>Add channel</ModalHeader>
            <ModalBody>
              <Field
                name='stagePropId'
                component={SingleSelectField}
                options={stageProps}
                label='Stage Prop'
                required
                validate={required}
              />

              <Field name='name' component={InputField} type='text' label='Channel Name' required validate={required} />
            </ModalBody>
            <ModalFooter>
              <Button type='submit' color='info' disabled={!valid}>
                Add channel
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

  addChannel(channel) {
    const { sequence } = this.props

    const displayOrder = _.findLastIndex(sequence.channels, it => it.stagePropId === channel.stagePropId) + 1

    this.props.addChannel({
      ...channel,
      id: uniqueId(),
      sequenceId: sequence.id,
      effects: [],
      displayOrder,
    })
  }

  hideModal() {
    this.props.hideAddChannelModal()
  }
}

const selector = formValueSelector(FORM_NAME)

function mapStateToProps(state) {
  const { sequenceEdit } = state.page
  const selectedStagePropUuid = selector(state, 'stagePropId')

  return {
    sequence: sequenceEdit.present.sequence,
    stageProps: sequenceEdit.present.stage.stageProps,
    visible: sequenceEdit.present.addChannelModalVisible,
    selectedStagePropUuid,
  }
}

AddChannelModal = connect(mapStateToProps, { addChannel, hideAddChannelModal })(AddChannelModal)
export default reduxForm({ form: FORM_NAME })(AddChannelModal)
