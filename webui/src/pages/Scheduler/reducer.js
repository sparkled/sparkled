import _ from 'lodash';
import { getResponseError } from '../../utils/reducerUtils';
import * as actionTypes from './actionTypes';

const initialState = {
  fetching: false,
  fetchError: null,
  adding: false,
  addError: null,
  deleting: false,
  deleteError: null,
  sequenceToDelete: null,
  scheduledSequences: []
};

export default (state = initialState, action) => {
  if (action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  switch (action.type) {
    case actionTypes.SHOW_ADD_MODAL:
      return { ...state, addModalVisible: true };

    case actionTypes.HIDE_ADD_MODAL:
      return { ...state, addModalVisible: false, addError: null };

    case actionTypes.HIDE_DELETE_MODAL:
      return { ...state, sequenceToDelete: null, deleteError: null };

    case actionTypes.SHOW_DELETE_MODAL:
      return { ...state, sequenceToDelete: action.payload.sequenceToDelete };

    case actionTypes.FETCH_SCHEDULED_SEQUENCES_PENDING:
      return { ...state, fetching: true, scheduledSequences: [] };

    case actionTypes.FETCH_SCHEDULED_SEQUENCES_FULFILLED:
      return { ...state, fetching: false, scheduledSequences: _.mapKeys(action.payload.data, 'id') };

    case actionTypes.FETCH_SCHEDULED_SEQUENCES_REJECTED:
      return { ...state, fetching: false, fetchError: getResponseError(action) };

    case actionTypes.ADD_SCHEDULED_SEQUENCE_PENDING:
      return { ...state, adding: true, addError: null };

    case actionTypes.ADD_SCHEDULED_SEQUENCE_FULFILLED:
      return { ...state, adding: false, addModalVisible: false };

    case actionTypes.ADD_SCHEDULED_SEQUENCE_REJECTED:
      return { ...state, adding: false, addError: getResponseError(action) };

    case actionTypes.DELETE_SCHEDULED_SEQUENCE_PENDING:
      return { ...state, deleting: true, deleteError: null };

    case actionTypes.DELETE_SCHEDULED_SEQUENCE_FULFILLED:
      return {
        ...state, sequenceToDelete: null,
        scheduledSequences: _.omit(state.data, action.payload.data.id), deleting: false,
      };

    case actionTypes.DELETE_SCHEDULED_SEQUENCE_REJECTED:
      return { ...state, deleting: false, deleteError: getResponseError(action) };

    default:
      return state;
  }
};
