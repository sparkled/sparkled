import * as schedulerActionTypes from './actionTypes';
import * as schedulerServiceActionTypes from '../../services/scheduledSong/actionTypes';

const initialState = {
  songToDelete: null
};

export default (state = initialState, action) => {
  switch (action.type) {
    case schedulerActionTypes.SHOW_ADD_MODAL:
      return {
        ...state,
        addModalVisible: true
      };

    case schedulerActionTypes.HIDE_ADD_MODAL:
    case `${schedulerServiceActionTypes.ADD_SCHEDULED_SONG}_FULFILLED`:
      return {
        ...state,
        addModalVisible: false
      };

    case schedulerActionTypes.HIDE_DELETE_MODAL:
    case `${schedulerServiceActionTypes.DELETE_SCHEDULED_SONG}_FULFILLED`:
      return {
        ...state,
        songToDelete: null
      };

    case schedulerActionTypes.SHOW_DELETE_MODAL:
      return {
        ...state,
        songToDelete: action.payload.songToDelete
      };

    default:
      return state;
  }
};
