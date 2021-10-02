import { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import { Dispatch } from 'redux'
import { failed, loaded } from '../../store/reducers/dashboardScreenReducer'
import { useAppSelector } from '../../store/store'
import { DashboardViewModel } from './apiTypes'
import useAxios, { isLoadingOrReloading } from './useAxios'

function useApiGetDashboard() {
  const axios = useAxios()
  const dispatch = useDispatch()
  const { requestStatus } = useAppSelector(state => state.dashboardScreen)

  useEffect(() => {
    let mounted = true

    if (isLoadingOrReloading(requestStatus)) {
      console.info(`${requestStatus} dashboard screen.`)

      axios.get<DashboardViewModel>('/dashboard').then(
        (response) => mounted && onSuccess(response.data, dispatch),
        (error) => mounted && onError(error, dispatch)
      )
    }
    return () => {
      mounted = false
    }
  }, [axios, dispatch, requestStatus])
}

function onSuccess(data: DashboardViewModel, dispatch: Dispatch) {
  console.info('Retrieved dashboard screen contents.')
  dispatch(loaded(data))
}

function onError(error: any, dispatch: Dispatch) {
  console.warn('Failed to retrieve dashboard screen contents.')
  dispatch(failed())
}

export default useApiGetDashboard
