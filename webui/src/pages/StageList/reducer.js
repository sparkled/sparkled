import produce from 'immer';
import { getResponseError } from '../../utils/reducerUtils';
import * as actionTypes from './actionTypes';

const initialState = {
  fetching: false,
  fetchError: null,
  adding: false,
  addError: null,
  deleting: false,
  deleteError: null,
  stages: [],
  stageToDelete: null,
  addModalVisible: false
};

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_STAGES_PENDING:
        draft.fetching = true;
        draft.fetchError = null;
        break;

      case actionTypes.FETCH_STAGES_FULFILLED:
        draft.fetching = false;
        draft.stages = action.payload.data;
        break;

      case actionTypes.FETCH_STAGES_REJECTED:
        draft.fetching = false;
        draft.fetchError = getResponseError(action);
        break;

      case actionTypes.ADD_STAGE_PENDING:
        draft.adding = true;
        draft.addError = null;
        break;

      case actionTypes.ADD_STAGE_FULFILLED:
        draft.adding = false;
        draft.addModalVisible = false;
        break;

      case actionTypes.ADD_STAGE_REJECTED:
        draft.adding = false;
        draft.addError = getResponseError(action);
        break;

      case actionTypes.DELETE_STAGE_PENDING:
        draft.deleting = true;
        draft.deleteError = null;
        break;

      case actionTypes.DELETE_STAGE_FULFILLED:
        draft.deleting = false;
        draft.stageToDelete = null;
        break;

      case actionTypes.DELETE_STAGE_REJECTED:
        draft.deleting = false;
        draft.deleteError = getResponseError(action);
        break;

      case actionTypes.SHOW_ADD_MODAL:
        draft.addModalVisible = true;
        break;

      case actionTypes.HIDE_ADD_MODAL:
        draft.addModalVisible = false;
        draft.addError = null;
        break;

      case actionTypes.HIDE_DELETE_MODAL:
        draft.stageToDelete = null;
        draft.deleteError = null;
        break;

      case actionTypes.SHOW_DELETE_MODAL:
        draft.stageToDelete = action.payload.stageToDelete;
        break;

      default:
        return;
    }
  });
};
