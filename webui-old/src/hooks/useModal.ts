import { useCallback } from 'react'
import { useDispatch } from 'react-redux'
import { ModalName, ModalState, setModalStatus } from '../store/reducers/modalReducer'
import { useAppSelector } from '../store/store'

export type ModalManager = {
  isOpen: boolean
  isLoading: boolean
  show: (state?: any) => {}
  hide: () => {}
  state: ModalState
}

function useModal(modal: ModalName) {
  const dispatch = useDispatch()
  const { modals } = useAppSelector(state => state.modal)

  const status = modals[modal].status
  const isOpen = status === 'open' || status === 'loading'
  const isLoading = status === 'loading'

  const showModal = useCallback((data: any) => {
    dispatch(setModalStatus({ modal, status: 'open', data }))
  }, [dispatch, modal])

  const hideModal = useCallback(() => {
    dispatch(setModalStatus({ modal, status: 'closed' }))
  }, [dispatch, modal])

  return { isOpen, isLoading, show: showModal, hide: hideModal, state: modals[modal] } as ModalManager
}

export default useModal
