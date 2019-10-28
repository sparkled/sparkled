import produce from 'immer';
import * as actionTypes from './actionTypes';
import { getResponseError } from '../../utils/reducerUtils';

const initialState = {
  fetching: false,
  fetchError: null,
  adding: false,
  addError: null,
  deleting: false,
  deleteError: null,
  sequenceToDelete: null,
  addModalVisible: false,
  sequences: []
};

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_SEQUENCES_PENDING:
        draft.fetching = true;
        draft.fetchError = null;
        break;

      case actionTypes.FETCH_SEQUENCES_FULFILLED:
        draft.fetching = false;
        draft.sequences = action.payload.data;
        break;

      case actionTypes.FETCH_SEQUENCES_REJECTED:
        draft.fetching = false;
        draft.fetchError = getResponseError(action);
        break;

      case actionTypes.ADD_SEQUENCE_PENDING:
        draft.adding = true;
        draft.addError = null;
        break;

      case actionTypes.ADD_SEQUENCE_FULFILLED:
        draft.adding = false;
        draft.addModalVisible = false;
        break;

      case actionTypes.ADD_SEQUENCE_REJECTED:
        draft.adding = false;
        draft.addError = getResponseError(action);
        break;

      case actionTypes.DELETE_SEQUENCE_PENDING:
        draft.deleting = true;
        draft.deleteError = null;
        break;

      case actionTypes.DELETE_SEQUENCE_FULFILLED:
        draft.deleting = false;
        draft.sequenceToDelete = null;
        break;

      case actionTypes.DELETE_SEQUENCE_REJECTED:
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

      case actionTypes.SHOW_DELETE_MODAL:
        draft.sequenceToDelete = action.payload.sequenceToDelete;
        break;

      case actionTypes.HIDE_DELETE_MODAL:
        draft.sequenceToDelete = null;
        draft.deleteError = null;
        break;

      default:
        return;
    }
  });
};
