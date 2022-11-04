import { DashboardViewModel } from '../apiTypes'
import useAxiosSwr, { AxiosSwr } from '../useAxiosSwr'

function useApiGetDashboard(): AxiosSwr<DashboardViewModel> {
  return useAxiosSwr<DashboardViewModel>(
    {
      url: '/dashboard'
    },
    true
  )
}

export default useApiGetDashboard
