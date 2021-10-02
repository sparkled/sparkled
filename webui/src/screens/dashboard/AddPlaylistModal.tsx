import React from 'react'
import { SubmitHandler, useForm } from 'react-hook-form'
import { useDispatch } from 'react-redux'
import Modal from '../../components/Modal'
import Form from '../../components/v2/Form'
import { PlaylistViewModel } from '../../hooks/api/apiTypes'
import useModal from '../../hooks/useModal'
import { addPlaylist } from '../../store/reducers/dashboardScreenReducer'

const AddPlaylistModal: React.FC = () => {
  const modal = useModal('playlistAdd')
  return <Impl key={modal.isOpen.toString()} />
}

const Impl: React.FC = () => {
  const modal = useModal('playlistAdd')
  const dispatch = useDispatch()
  const { register, handleSubmit, formState: { errors } } = useForm<PlaylistViewModel>({ mode: 'onChange' })

  const submit: SubmitHandler<PlaylistViewModel> = (data: PlaylistViewModel) => {
    dispatch(addPlaylist(data))
  }

  const buttons = (
      <Form.Button type='submit' loading={modal.isLoading}>Create</Form.Button>
  )

  return (
    <Modal title='Add playlist' modal={modal} buttons={buttons} onSubmit={handleSubmit(submit)} disabled={modal.isLoading}>
      <Form.Label>Name</Form.Label>
      <Form.Input type='text' autoFocus autoComplete='off' {...register('name', { required: true })} />
      {errors.name && <span>Name is required</span>}
    </Modal>
  )
}

export default AddPlaylistModal
