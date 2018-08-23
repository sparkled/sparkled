import _ from 'lodash';
import * as actionTypes from './actionTypes';

const initialState = {
  fetching: false,
  fetchError: null,
  adding: false,
  addError: null,
  deleting: false,
  deleteError: null,
  songToDelete: null,
  addModalVisible: false
};

export default (state = initialState, action) => {
  switch (action.type) {

    case `${actionTypes.FETCH_SONGS}_PENDING`:
      return {
        ...state,
        fetching: true,
        fetchError: null
      };

    case `${actionTypes.FETCH_SONGS}_FULFILLED`:
      return {
        ...state,
        fetching: false,
        songs: _.mapKeys(action.payload.data, 'id')
      };

    case `${actionTypes.FETCH_SONGS}_REJECTED`:
      return {
        ...state,
        fetching: false,
        fetchError: action.payload.response.data
      };

    case `${actionTypes.ADD_SONG}_PENDING`:
      return {
        ...state,
        adding: true,
        addError: null
      };

    case `${actionTypes.ADD_SONG}_FULFILLED`:
      return {
        ...state,
        adding: false,
        addModalVisible: false
      };

    case `${actionTypes.ADD_SONG}_REJECTED`:
      return {
        ...state,
        adding: false,
        addError: action.payload.response.data
      };

    case `${actionTypes.DELETE_SONG}_PENDING`:
      return {
        ...state,
        deleting: true,
        deleteError: null
      };

    case `${actionTypes.DELETE_SONG}_FULFILLED`:
      return {
        ...state,
        data: _.omit(state.data, action.payload.data.id),
        deleting: false,
        songToDelete: null
      };

    case `${actionTypes.DELETE_SONG}_REJECTED`:
      return {
        ...state,
        deleting: false,
        deleteError: action.payload.response.data
      };

    case actionTypes.SHOW_ADD_MODAL:
      return {
        ...state,
        addModalVisible: true
      };

    case actionTypes.HIDE_ADD_MODAL:
      return {
        ...state,
        addModalVisible: false,
        addError: null
      };

    case actionTypes.SHOW_DELETE_MODAL:
      return {
        ...state,
        songToDelete: action.payload.songToDelete
      };

    case actionTypes.HIDE_DELETE_MODAL:
      return {
        ...state,
        songToDelete: null,
        deleteError: null
      };

    default:
      return state;
  }
};
