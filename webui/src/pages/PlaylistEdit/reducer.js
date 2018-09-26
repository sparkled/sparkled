import _ from 'lodash';
import * as actionTypes from './actionTypes';
import { getResponseError } from '../../utils/reducerUtils';

const initialState = {
  fetching: false,
  fetchError: null,
  playlists: []
};

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  switch (action.type) {
    case actionTypes.FETCH_PLAYLISTS_PENDING:
      return { ...state, fetching: true, fetchError: null };

    case actionTypes.FETCH_PLAYLISTS_FULFILLED:
      return { ...state, fetching: false, playlists: _.mapKeys(action.payload.data, 'id'), selectedStagePropUuid: null };

    case actionTypes.FETCH_PLAYLISTS_REJECTED:
      return { ...state, fetching: false, fetchError: getResponseError(action) };

    default:
      return state;
  }
};

