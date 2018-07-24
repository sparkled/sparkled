import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchScheduledSongs = date => {
  const request = axios.get(`${restConfig.ROOT_URL}/scheduledSongs`, { params: { date } });

  return {
    type: actionTypes.FETCH_SCHEDULED_SONGS,
    payload: request
  };
};

export const addScheduledSong = scheduledSong => {
  const url = `${restConfig.ROOT_URL}/scheduledSongs`;
  const request = axios.post(url, scheduledSong);

  return {
    type: actionTypes.ADD_SCHEDULED_SONG,
    payload: request
  };
};

export const deleteScheduledSong = scheduledSongId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/scheduledSongs/${scheduledSongId}`);

  return {
    type: actionTypes.DELETE_SCHEDULED_SONG,
    payload: request
  };
};
