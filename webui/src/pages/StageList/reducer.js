import _ from 'lodash';
import * as actionTypes from './actionTypes';
import { getResponseError } from '../../utils/reducerUtils';

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
  switch (action.type) {
    case `${actionTypes.FETCH_STAGES}_PENDING`:
      return {
        ...state,
        fetching: true,
        fetchError: null
      };

    case `${actionTypes.FETCH_STAGES}_FULFILLED`:
      return {
        ...state,
        fetching: false,
        stages: _.mapKeys(action.payload.data, 'id')
      };

    case `${actionTypes.FETCH_STAGES}_REJECTED`:
      return {
        ...state,
        fetching: false,
        fetchError: getResponseError(action)
      };

    case `${actionTypes.ADD_STAGE}_PENDING`:
      return {
        ...state,
        adding: true,
        addError: null
      };

    case `${actionTypes.ADD_STAGE}_FULFILLED`:
      return {
        ...state,
        adding: false,
        addModalVisible: false
      };

    case `${actionTypes.ADD_STAGE}_REJECTED`:
      return {
        ...state,
        adding: false,
        addError: getResponseError(action)
      };

    case `${actionTypes.DELETE_STAGE}_PENDING`:
      return {
        ...state,
        deleting: true,
        deleteError: null
      };

    case `${actionTypes.DELETE_STAGE}_FULFILLED`:
      return {
        ...state,
        data: _.omit(state.data, action.payload.data.id),
        deleting: false,
        stageToDelete: null
      };

    case `${actionTypes.DELETE_STAGE}_REJECTED`:
      return {
        ...state,
        deleting: false,
        deleteError: getResponseError(action)
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

    case actionTypes.HIDE_DELETE_MODAL:
      return {
        ...state,
        stageToDelete: null,
        deleteError: null
      };

    case actionTypes.SHOW_DELETE_MODAL:
      return {
        ...state,
        stageToDelete: action.payload.stageToDelete
      };

    default:
      return state;
  }
};
