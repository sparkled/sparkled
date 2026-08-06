import { produce } from 'immer'
import _ from 'lodash'
import * as actionTypes from './actionTypes'
import { getResponseError } from '../../utils/reducerUtils'

const initialState = {
  fetchingPlaylist: false,
  fetchingSequences: false,
  fetchPlaylistError: null,
  fetchSequencesError: null,
  saving: false,
  savingError: null,
  playlist: null,
  sequences: [],
  addSequenceModalVisible: false,
}

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state
  }

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_PLAYLIST_PENDING:
        draft.fetchingPlaylist = true
        draft.fetchPlaylistError = null
        break

      case actionTypes.FETCH_PLAYLIST_FULFILLED:
        draft.fetchingPlaylist = false
        draft.playlist = action.payload.data
        break

      case actionTypes.FETCH_PLAYLIST_REJECTED:
        draft.fetchingPlaylist = false
        draft.fetchPlaylistError = getResponseError(action)
        break

      case actionTypes.FETCH_SEQUENCES_PENDING:
        draft.fetchingSequences = true
        draft.fetchSequenceError = null
        break

      case actionTypes.FETCH_SEQUENCES_FULFILLED:
        draft.fetchingSequences = false
        draft.sequences = action.payload.data
        break

      case actionTypes.FETCH_SEQUENCES_REJECTED:
        draft.fetchingSequences = false
        draft.fetchSequenceError = getResponseError(action)
        break

      case actionTypes.SAVE_PLAYLIST_PENDING:
        draft.saving = true
        draft.saveError = null
        break

      case actionTypes.SAVE_PLAYLIST_FULFILLED:
        draft.saving = false
        break

      case actionTypes.SAVE_PLAYLIST_REJECTED:
        draft.saving = false
        draft.saveError = getResponseError(action)
        break

      case actionTypes.ADD_PLAYLIST_SEQUENCE:
        draft.playlist.sequences = _.reject(draft.playlist.sequences, {
          id: action.payload.id,
        })
        break

      case actionTypes.DELETE_PLAYLIST_SEQUENCE:
        draft.playlist.sequences = _.reject(draft.playlist.sequences, {
          id: action.payload.id,
        })
        break

      case actionTypes.UPDATE_PLAYLIST_SEQUENCE:
        const { playlistSequence } = action.payload
        const index = _.findIndex(draft.playlist.sequences, {
          id: playlistSequence.id,
        })
        draft.playlist.sequences[index] = playlistSequence
        break

      case actionTypes.SHOW_ADD_SEQUENCE_MODAL:
        draft.addSequenceModalVisible = true
        break

      case actionTypes.HIDE_ADD_SEQUENCE_MODAL:
        draft.addSequenceModalVisible = false
        break

      case actionTypes.ADD_SEQUENCE:
        draft.addSequenceModalVisible = false
        draft.playlist.sequences.push(action.payload.sequence)
        break

      default:
        return
    }
  })
}
