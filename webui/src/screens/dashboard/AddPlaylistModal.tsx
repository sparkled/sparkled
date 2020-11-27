import React from 'react'
import Modal from './Modal'
import useDashboardModals from './useDashboardModals'

type Props = {
  className?: string
}

const AddPlaylistModal: React.FC<Props> = props => {
  const { isModalOpen } = useDashboardModals()

  const buttons = (
    <>
      <button>Close</button>
      <button>Add Playlist</button>
    </>
  )

  return (
    <Modal className={props.className} isOpen={isModalOpen('playlistAdd')} title='Add playlist' buttons={buttons}>
      {props.children}
    </Modal>
  )
}

export default AddPlaylistModal
