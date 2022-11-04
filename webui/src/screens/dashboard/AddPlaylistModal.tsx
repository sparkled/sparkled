import React from 'react'
import { Button, FloatingLabel, Form } from 'react-bootstrap'
import { SubmitHandler, useForm } from 'react-hook-form'
import SparkledButton from '../../components/SparkledButton'
import SparkledModal from '../../components/SparkledModal'
import { PlaylistViewModel } from '../../hooks/api/apiTypes'
import useApiCreatePlaylist from '../../hooks/api/playlists/useApiCreatePlaylist'
import { getApiError } from '../../utils/errorUtils'

type Props = {
  show?: boolean
  onHide?: () => void
}

const AddPlaylistModal: React.FC<Props> = ({ show, onHide }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<PlaylistViewModel>({ mode: 'onChange' })
  const { createPlaylist, submitting } = useApiCreatePlaylist()

  const submit: SubmitHandler<PlaylistViewModel> = async (playlist: PlaylistViewModel) => {
    try {
      await createPlaylist(playlist)
    } catch (e) {
      // TODO show error.
      console.error(getApiError(e))
    }
  }

  return (
    <SparkledModal show={show} onHide={onHide}>
      <form onSubmit={handleSubmit(submit)}>
        <SparkledModal.Header>
          <SparkledModal.Title>Add playlist</SparkledModal.Title>
        </SparkledModal.Header>

        <SparkledModal.Body className='px-4 pb-0'>
            <FloatingLabel label='Name' className='w-100 mb-3'>
              <Form.Control type='text' placeholder='Name' {...register('name', { required: true })} />
            </FloatingLabel>
            {errors.name && <span>Name is required</span>}
        </SparkledModal.Body>

        <SparkledModal.Footer>
          <SparkledButton variant='primary' type='submit' submitting={submitting}>Create</SparkledButton>
        </SparkledModal.Footer>
      </form>
    </SparkledModal>
  )
}

export default AddPlaylistModal
