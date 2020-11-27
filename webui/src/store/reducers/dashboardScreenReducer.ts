import { AnyAction, createAction, createReducer } from '@reduxjs/toolkit'
import { DashboardViewModel, RequestStatus } from '../../hooks/api/apiTypes'

export type State = {
  requestStatus: RequestStatus
  dashboard: DashboardViewModel | null
  query: string

  /**
   * Status === 0 means the modal is hidden, status !== 0 means the modal is visible, and the number can be a model ID.
   */
  modalStatus: Partial<Record<DashboardModalName, number>>
}

export type DashboardModalName =
  | 'playlistAdd'
  | 'playlistDelete'
  | 'scheduledTaskAdd'
  | 'scheduledTaskDelete'
  | 'sequenceAdd'
  | 'sequenceDelete'
  | 'songAdd'
  | 'songDelete'
  | 'stageAdd'
  | 'stageDelete'

const initialState: State = {
  requestStatus: 'loading',
  dashboard: null,
  query: '',
  modalStatus: {},
}

export const reload = createAction('dashboardScreen/reload')
export const loading = createAction('dashboardScreen/loading')
export const loaded = createAction<DashboardViewModel>('dashboardScreen/loaded')
export const failed = createAction('dashboardScreen/failed')
export const setModalStatus = createAction<{ modal: DashboardModalName; value: number }>(
  'dashboardScreen/setModalStatus'
)
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

  builder.addCase(setModalStatus, (state, action) => {
    state.modalStatus[action.payload.modal] = action.payload.value
  })

  builder.addCase(setQuery, (state, action) => {
    state.query = action.payload
  })

  // TODO this is a workaround to refresh the Dashboard screen whenever any API
  // action completes.
  function isActionWithNumberPayload(action: AnyAction): action is AnyAction {
    return action.type.endsWith('FULFILLED')
  }

  builder.addMatcher(isActionWithNumberPayload, state => {
    state.requestStatus = 'reloading'
  })
})
