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

export const addSong = songData => {
  const url = `${restConfig.ROOT_URL}/songs`;

  const formData = new FormData();
  formData.append('song', JSON.stringify(songData.song));
  formData.append('mp3', songData.mp3);

  const config = { headers: { 'content-type': 'multipart/form-data' }};
  const request = axios.post(url, formData, config);

  return {
    type: actionTypes.ADD_SONG,
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
