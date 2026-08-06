import React from 'react'
import { SubmitHandler, useForm } from 'react-hook-form'
import { useDispatch } from 'react-redux'
import Modal from '../../components/Modal'
import Form from '../../components/v2/Form'
import { PlaylistSearchViewModel } from '../../hooks/api/apiTypes'
import useModal from '../../hooks/useModal'
import { deletePlaylist } from '../../store/reducers/dashboardScreenReducer'

const DeletePlaylistModal: React.FC = () => {
  const modal = useModal('playlistDelete')
  return <Impl key={modal.isOpen.toString()} />
}

const Impl: React.FC = () => {
  const modal = useModal('playlistDelete')
  const dispatch = useDispatch()
  const { handleSubmit } = useForm<never>()

  const playlist = modal.state.data as PlaylistSearchViewModel

  if (!playlist) {
    return <></>
  }

  const submit: SubmitHandler<never> = () => {
    dispatch(deletePlaylist(playlist))
  }

  const buttons = (
    <Form.Button type='submit' loading={modal.isLoading}>
      Delete
    </Form.Button>
  )

  return (
    <Modal
      title='Delete playlist'
      modal={modal}
      buttons={buttons}
      onSubmit={handleSubmit(submit)}
      disabled={modal.isLoading}
    >
      Are you sure you want to delete playlist <b>{playlist?.name}</b>?
    </Modal>
  )
}

export default DeletePlaylistModal
