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
  sequences: {}
};

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  switch (action.type) {
    case actionTypes.FETCH_SEQUENCES_PENDING:
      return { ...state, fetching: true, fetchError: null, sequences: {} };

    case actionTypes.FETCH_SEQUENCES_FULFILLED:
      return { ...state, fetching: false, sequences: action.payload.data };

    case actionTypes.FETCH_SEQUENCES_REJECTED:
      return { ...state, fetching: false, fetchError: getResponseError(action) };

    case actionTypes.ADD_SEQUENCE_PENDING:
      return { ...state, adding: true, addError: null };

    case actionTypes.ADD_SEQUENCE_FULFILLED:
      return { ...state, adding: false, addModalVisible: false };

    case actionTypes.ADD_SEQUENCE_REJECTED:
      return { ...state, adding: false, addError: getResponseError(action) };

    case actionTypes.DELETE_SEQUENCE_PENDING:
      return { ...state, deleting: true, deleteError: null };

    case actionTypes.DELETE_SEQUENCE_FULFILLED:
      return { ...state, deleting: false, sequenceToDelete: null };

    case actionTypes.DELETE_SEQUENCE_REJECTED:
      return { ...state, deleting: false, deleteError: getResponseError(action) };

    case actionTypes.SHOW_ADD_MODAL:
      return { ...state, addModalVisible: true };

    case actionTypes.HIDE_ADD_MODAL:
      return { ...state, addModalVisible: false, addError: null };

    case actionTypes.SHOW_DELETE_MODAL:
      return { ...state, sequenceToDelete: action.payload.sequenceToDelete };

    case actionTypes.HIDE_DELETE_MODAL:
      return { ...state, sequenceToDelete: null, deleteError: null };

    default:
      return state;
  }
};
