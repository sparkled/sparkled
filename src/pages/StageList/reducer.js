import * as stageListActionTypes from './actionTypes';
import * as stageActionTypes from '../../services/stage/actionTypes';

const initialState = {
  stageToDelete: null,
  addModalVisible: false
};

export default (state = initialState, action) => {
  switch (action.type) {
    case stageListActionTypes.SHOW_ADD_MODAL:
      return {
        ...state,
        addModalVisible: true
      };

    case stageListActionTypes.HIDE_ADD_MODAL:
    case `${stageActionTypes.ADD_STAGE}_FULFILLED`:
      return {
        ...state,
        addModalVisible: false
      };

    case stageListActionTypes.HIDE_DELETE_MODAL:
    case `${stageActionTypes.DELETE_STAGE}_FULFILLED`:
      return {
        ...state,
        stageToDelete: null
      };

    case stageListActionTypes.SHOW_DELETE_MODAL:
      return {
        ...state,
        stageToDelete: action.payload.stageToDelete
      };

    default:
      return state;
  }
};
