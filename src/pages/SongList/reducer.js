import * as songListActionTypes from './actionTypes';
import * as songActionTypes from '../../services/song/actionTypes';

const initialState = {
  songToDelete: null
};

export default (state = initialState, action) => {
  switch (action.type) {
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
