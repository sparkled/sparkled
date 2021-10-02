import { useCallback } from 'react'
import { useDispatch } from 'react-redux'
import { ModalName, ModalState, setModalStatus } from '../store/reducers/modalReducer'
import { useAppSelector } from '../store/store'

export type ModalManager = {
  isOpen: boolean
  isLoading: boolean
  showModal: () => {}
  hideModal: () => {}
  state: ModalState
}

function useModal(modal: ModalName) {
  const dispatch = useDispatch()
  const { modals } = useAppSelector(state => state.modal)

  const status = modals[modal].status
  const isOpen = status === 'open' || status === 'loading'
  const isLoading = status === 'loading'

  const showModal = useCallback((value?: string) => {
    dispatch(setModalStatus({ modal, status: 'open' }))
  }, [dispatch, modal])

  const hideModal = useCallback(() => {
    dispatch(setModalStatus({ modal, status: 'closed' }))
  }, [dispatch, modal])

  return { isOpen, isLoading, showModal, hideModal, state: modals[modal] } as ModalManager
}

export default useModal
