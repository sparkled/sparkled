import { mapKeys } from 'lodash-es';
import * as actionTypes from './actionTypes';

export default (state = {}, action) => {
  switch (action.type) {
    case actionTypes.FETCH_SONGS:
      return mapKeys(action.payload.data, 'id');
    default:
      return state;
  }
};
