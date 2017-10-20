import _ from 'lodash';
import * as actionTypes from './actionTypes';

export default (state = {}, action) => {
  switch (action.type) {
    case `${actionTypes.FETCH_SONGS}_PENDING`:
      return {
        ...state,
        fetching: true,
        data: []
      };

    case `${actionTypes.FETCH_SONGS}_FULFILLED`:
      return {
        ...state,
        fetching: false,
        data: _.mapKeys(action.payload.data, 'id')
      };

    case `${actionTypes.FETCH_SONGS}_REJECTED`:
      return {
        ...state,
        fetching: false,
        fetchError: action.payload.response.data,
        data: []
      };

    case `${actionTypes.DELETE_SONG}_PENDING`:
      return {
        ...state,
        deleting: true,
        deleteError: null,
        deleteSuccess: null
      };

    case `${actionTypes.DELETE_SONG}_FULFILLED`:
      return {
        ...state,
        data: _.omit(state.data, action.payload.data.id),
        deleting: false,
        deleteError: null,
        deleteSuccess: true
      };

    case `${actionTypes.DELETE_SONG}_REJECTED`:
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
