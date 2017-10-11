import _ from 'lodash';
import * as actionTypes from './actionTypes';

export default (state = {}, action) => {
  switch (action.type) {
    case actionTypes.FETCH_SONGS:
      return _.mapKeys(action.payload.data, 'id');
    default:
      return state;
  }
};
