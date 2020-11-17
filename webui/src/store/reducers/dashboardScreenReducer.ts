import { AnyAction, createAction, createReducer } from '@reduxjs/toolkit'
import { DashboardViewModel, RequestStatus } from '../../hooks/api/apiTypes'

export type State = {
  requestStatus: RequestStatus
  dashboard: DashboardViewModel | null
  query: string
}

const initialState: State = {
  requestStatus: 'loading',
  dashboard: null,
  query: '',
}

export const reload = createAction('dashboardScreen/reload')
export const loading = createAction('dashboardScreen/loading')
export const loaded = createAction<DashboardViewModel>('dashboardScreen/loaded')
export const failed = createAction('dashboardScreen/failed')
export const setQuery = createAction<string>('dashboardScreen/setQuery')

export default createReducer(initialState, builder => {
  builder.addCase(reload, state => {
    state.requestStatus = 'reloading'
  })

  builder.addCase(loading, state => {
    state.requestStatus = 'loading'
  })

  builder.addCase(loaded, (state, action) => {
    state.requestStatus = 'success'
    state.dashboard = action.payload
  })

  builder.addCase(failed, state => {
    state.requestStatus = 'fail'
  })

  builder.addCase(setQuery, (state, action) => {
    state.query = action.payload
  })

  function isActionWithNumberPayload(action: AnyAction): action is AnyAction {
    return action.type.endsWith('FULFILLED')
  }

  builder.addMatcher(isActionWithNumberPayload, state => {
    state.requestStatus = 'reloading'
  })
})
