import _ from 'lodash';
import * as actionTypes from './actionTypes';

const initialState = {
  fetching: false,
  fetchError: null,
  fetchSuccess: null,
  adding: false,
  addError: null,
  addSuccess: null,
  deleting: false,
  deleteError: null,
  deleteSuccess: null,
  data: []
};

export default (state = initialState, action) => {
  switch (action.type) {
    case `${actionTypes.FETCH_SCHEDULED_SONGS}_PENDING`:
      return {
        ...state,
        fetching: true,
        data: []
      };

    case `${actionTypes.FETCH_SCHEDULED_SONGS}_FULFILLED`:
      return {
        ...state,
        fetching: false,
        data: _.mapKeys(action.payload.data, 'id')
      };

    case `${actionTypes.FETCH_SCHEDULED_SONGS}_REJECTED`:
      return {
        ...state,
        fetching: false,
        fetchError: action.payload.response.data,
        data: []
      };

    case `${actionTypes.ADD_SCHEDULED_SONG}_PENDING`:
      return {
        ...state,
        adding: true,
        addError: null,
        addSuccess: null
      };

    case `${actionTypes.ADD_SCHEDULED_SONG}_FULFILLED`:
      return {
        ...state,
        adding: false,
        addError: null,
        addSuccess: true
      };

    case `${actionTypes.ADD_SCHEDULED_SONG}_REJECTED`:
      return {
        ...state,
        adding: false,
        addError: action.payload.response.data,
        addSuccess: null
      };

    case `${actionTypes.DELETE_SCHEDULED_SONG}_PENDING`:
      return {
        ...state,
        deleting: true,
        deleteError: null,
        deleteSuccess: null
      };

    case `${actionTypes.DELETE_SCHEDULED_SONG}_FULFILLED`:
      return {
        ...state,
        data: _.omit(state.data, action.payload.data.id),
        deleting: false,
        deleteError: null,
        deleteSuccess: true
      };

    case `${actionTypes.DELETE_SCHEDULED_SONG}_REJECTED`:
      return {
        ...state,
        deleting: false,
        deleteError: action.payload.response.data,
        deleteSuccess: null
      };

    default:
      return state;
  }
};
