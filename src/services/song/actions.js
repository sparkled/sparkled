import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchSongs = () => {
  const request = axios.get(`${restConfig.ROOT_URL}/songs`);

  return {
    type: actionTypes.FETCH_SONGS,
    payload: request
  };
};

export const deleteSong = songId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/songs/${songId}`);

  return {
    type: actionTypes.DELETE_SONG,
    payload: request
  };
};
