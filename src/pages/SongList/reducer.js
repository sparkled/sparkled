import * as songListActionTypes from './actionTypes';
import * as songActionTypes from '../../services/song/actionTypes';

const initialState = {
  songToDelete: null,
  addModalVisible: false
};

export default (state = initialState, action) => {
  switch (action.type) {
    case songListActionTypes.SHOW_ADD_MODAL:
      return {
        ...state,
        addModalVisible: true
      };

    case songListActionTypes.HIDE_ADD_MODAL:
    case `${songActionTypes.ADD_SONG}_FULFILLED`:
      return {
        ...state,
        addModalVisible: false
      };

    case songListActionTypes.HIDE_DELETE_MODAL:
    case `${songActionTypes.DELETE_SONG}_FULFILLED`:
      return {
        ...state,
        songToDelete: null
      };

    case songListActionTypes.SHOW_DELETE_MODAL:
      return {
        ...state,
        songToDelete: action.payload.songToDelete
      };

    default:
      return state;
  }
};
