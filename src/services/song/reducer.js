import _ from 'lodash';
import * as actionTypes from './actionTypes';

export default (state = {}, action) => {
  switch (action.type) {
    case `${actionTypes.FETCH_SONGS}_PENDING`:
      return {
        loading: true,
        data: []
      };

    case `${actionTypes.FETCH_SONGS}_FULFILLED`:
      return {
        data: _.mapKeys(action.payload.data, 'id')
      };

    case `${actionTypes.FETCH_SONGS}_REJECTED`:
      return {
        error: action.payload.response.data,
        data: []
      };

    default:
      return state;
  }
};
