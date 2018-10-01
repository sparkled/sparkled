import _ from 'lodash';
import * as actionTypes from './actionTypes';
import { getResponseError } from '../../utils/reducerUtils';

const initialState = {
  fetching: false,
  fetchError: null,
  adding: false,
  addError: null,
  addModalVisible: false,
  deleting: false,
  playlistToDelete: null,
  deleteError: null,
  deleteModalVisible: false,
  playlists: []
};

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  switch (action.type) {
    case actionTypes.FETCH_PLAYLISTS_PENDING:
      return { ...state, fetching: true, fetchError: null };

    case actionTypes.FETCH_PLAYLISTS_FULFILLED:
      return { ...state, fetching: false, playlists: action.payload.data };

    case actionTypes.FETCH_PLAYLISTS_REJECTED:
      return { ...state, fetching: false, fetchError: getResponseError(action) };

    case actionTypes.ADD_PLAYLIST_PENDING:
      return { ...state, adding: true, addError: null };

    case actionTypes.ADD_PLAYLIST_FULFILLED:
      return { ...state, adding: false, addModalVisible: false };

    case actionTypes.ADD_PLAYLIST_REJECTED:
      return { ...state, adding: false, addError: getResponseError(action) };

    case actionTypes.DELETE_PLAYLIST_PENDING:
      return { ...state, deleting: true, deleteError: null };

    case actionTypes.DELETE_PLAYLIST_FULFILLED:
      return { ...state, data: _.omit(state.data, action.payload.data.id), deleting: false, playlistToDelete: null };

    case actionTypes.DELETE_PLAYLIST_REJECTED:
      return { ...state, deleting: false, deleteError: getResponseError(action) };

    case actionTypes.SHOW_ADD_MODAL:
      return { ...state, addModalVisible: true };

    case actionTypes.HIDE_ADD_MODAL:
      return { ...state, addModalVisible: false, addError: null };

    case actionTypes.SHOW_DELETE_MODAL:
      return { ...state, playlistToDelete: action.payload.playlistToDelete };

    case actionTypes.HIDE_DELETE_MODAL:
      return { ...state, playlistToDelete: null, deleteError: null };

    default:
      return state;
  }
};

