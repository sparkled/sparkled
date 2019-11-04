import produce from 'immer'
import * as actionTypes from './actionTypes'
import { getResponseError } from '../../utils/reducerUtils'

const initialState = {
  fetching: false,
  fetchError: null,
  adding: false,
  addError: null,
  addModalVisible: false,
  deleting: false,
  scheduledJobToDelete: null,
  deleteError: null,
  deleteModalVisible: false,
  scheduledJobs: []
}

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state
  }

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_SCHEDULED_JOBS_PENDING:
        draft.fetching = true
        draft.fetchError = null
        break

      case actionTypes.FETCH_SCHEDULED_JOBS_FULFILLED:
        draft.fetching = false
        draft.scheduledJobs = action.payload.data
        break

      case actionTypes.FETCH_SCHEDULED_JOBS_REJECTED:
        draft.fetching = false
        draft.fetchError = getResponseError(action)
        break

      case actionTypes.ADD_SCHEDULED_JOB_PENDING:
        draft.adding = true
        draft.addError = null
        break

      case actionTypes.ADD_SCHEDULED_JOB_FULFILLED:
        draft.adding = false
        draft.addModalVisible = false
        break

      case actionTypes.ADD_SCHEDULED_JOB_REJECTED:
        draft.adding = false
        draft.addError = getResponseError(action)
        break

      case actionTypes.DELETE_SCHEDULED_JOB_PENDING:
        draft.deleting = true
        draft.deleteError = null
        break

      case actionTypes.DELETE_SCHEDULED_JOB_FULFILLED:
        draft.deleting = false
        draft.scheduledJobToDelete = null
        break

      case actionTypes.DELETE_SCHEDULED_JOB_REJECTED:
        draft.deleting = false
        draft.deleteError = getResponseError(action)
        break

      case actionTypes.SHOW_ADD_MODAL:
        draft.addModalVisible = true
        break

      case actionTypes.HIDE_ADD_MODAL:
        draft.addModalVisible = false
        draft.addError = null
        break

      case actionTypes.SHOW_DELETE_MODAL:
        draft.scheduledJobToDelete = action.payload.scheduledJobToDelete
        break

      case actionTypes.HIDE_DELETE_MODAL:
        draft.scheduledJobToDelete = null
        draft.deleteError = null
        break

      default:
        return
    }
  })
}
