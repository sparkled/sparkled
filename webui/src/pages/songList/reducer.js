import { produce } from 'immer'
import * as actionTypes from './actionTypes'
import { getResponseError } from '../../utils/reducerUtils'

const initialState = {
  fetching: false,
  fetchError: null,
  adding: false,
  addError: null,
  deleting: false,
  deleteError: null,
  songToDelete: null,
  addModalVisible: false,
  songs: []
}

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state
  }

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_SONGS_PENDING:
        draft.fetching = true
        draft.fetchError = null
        break

      case actionTypes.FETCH_SONGS_FULFILLED:
        draft.fetching = false
        draft.songs = action.payload.data
        break

      case actionTypes.FETCH_SONGS_REJECTED:
        draft.fetching = false
        draft.fetchError = getResponseError(action)
        break

      case actionTypes.ADD_SONG_PENDING:
        draft.adding = true
        draft.addError = null
        break

      case actionTypes.ADD_SONG_FULFILLED:
        draft.adding = false
        draft.addModalVisible = false
        break

      case actionTypes.ADD_SONG_REJECTED:
        draft.adding = false
        draft.addError = getResponseError(action)
        break

      case actionTypes.DELETE_SONG_PENDING:
        draft.deleting = true
        draft.deleteError = null
        break

      case actionTypes.DELETE_SONG_FULFILLED:
        draft.deleting = false
        draft.songToDelete = null
        break

      case actionTypes.DELETE_SONG_REJECTED:
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
        draft.songToDelete = action.payload.songToDelete
        break

      case actionTypes.HIDE_DELETE_MODAL:
        draft.songToDelete = null
        draft.deleteError = null
        break

      default:
        return
    }
  })
}
