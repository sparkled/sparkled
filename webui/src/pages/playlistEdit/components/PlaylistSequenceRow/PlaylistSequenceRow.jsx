import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Field, reduxForm } from 'redux-form'
import { Button } from 'reactstrap'
import { deletePlaylistSequence, updatePlaylistSequence } from '../../actions'
import SingleSelectField from '../../../../components/form/SingleSelectField'
import { required } from '../../../../components/form/validators'

const toNumber = value => (!value ? null : Number(value))

class PlaylistSequenceRow extends Component {
  constructor(props) {
    super(props)
    this.updatePlaylistSequence = this.updatePlaylistSequence.bind(this)
  }

  componentDidMount() {
    const { initialize, playlistSequence } = this.props
    initialize(playlistSequence)
  }

  render() {
    const { deletePlaylistSequence, playlistSequence, sequences } = this.props

    return (
      <tr>
        <td width='66%'>
          <form>
            <Field
              name='sequenceId'
              className='m-0'
              component={SingleSelectField}
              options={sequences}
              parse={toNumber}
              required
              validate={required}
              onChange={this.updatePlaylistSequence}
            />
          </form>
        </td>
        <td>
          <Button color='danger' onClick={() => deletePlaylistSequence(playlistSequence.id)}>
            Delete
          </Button>
        </td>
      </tr>
    )
  }

  updatePlaylistSequence(event, newValue, previousValue, name) {
    const { playlistSequence, updatePlaylistSequence } = this.props
    updatePlaylistSequence({ ...playlistSequence, [name]: newValue })
  }
}

function mapStateToProps({ page }) {
  const { present } = page.playlistEdit
  const { sequences } = present
  return { sequences }
}

PlaylistSequenceRow = connect(mapStateToProps, { deletePlaylistSequence, updatePlaylistSequence })(PlaylistSequenceRow)
export default reduxForm({})(PlaylistSequenceRow)
