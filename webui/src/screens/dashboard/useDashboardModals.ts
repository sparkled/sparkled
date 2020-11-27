import { useCallback } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { DashboardModalName, setModalStatus } from '../../store/reducers/dashboardScreenReducer'
import { AppState } from '../../store/reducers/rootReducer'

function useDashboardModals() {
  const dispatch = useDispatch()
  const { modalStatus } = useSelector((state: AppState) => state.dashboardScreen)

  const isModalOpen = useCallback((modal: DashboardModalName) => {
    return (modalStatus[modal] ?? 0) > 0
  }, [modalStatus])

  const showModal = useCallback((modal: DashboardModalName, value?: number) => {
    dispatch(setModalStatus({ modal, value: value ?? 1 }))
  }, [dispatch])

  const hideModal = useCallback((modal: DashboardModalName) => {
    dispatch(setModalStatus({ modal, value: 0 }))
  }, [dispatch])

  return { isModalOpen, showModal, hideModal }
}

export default useDashboardModals
