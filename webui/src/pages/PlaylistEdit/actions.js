import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchPlaylists = stageId => {
  const request = axios.get(`${restConfig.ROOT_URL}/playlists`);

  return {
    type: actionTypes.FETCH_PLAYLISTS,
    payload: request
  };
};
