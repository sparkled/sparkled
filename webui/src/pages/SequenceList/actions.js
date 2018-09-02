import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchSequences = () => {
  const request = axios.get(`${restConfig.ROOT_URL}/sequences`);

  return {
    type: actionTypes.FETCH_SEQUENCES,
    payload: request
  };
};

export const addSequence = sequenceData => {
  const url = `${restConfig.ROOT_URL}/sequences`;

  const formData = new FormData();
  formData.append('sequence', JSON.stringify(sequenceData.sequence));
  formData.append('mp3', sequenceData.mp3);

  const config = { headers: { 'content-type': 'multipart/form-data' }};
  const request = axios.post(url, formData, config);

  return {
    type: actionTypes.ADD_SEQUENCE,
    payload: request
  };
};

export const deleteSequence = sequenceId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/sequences/${sequenceId}`);

  return {
    type: actionTypes.DELETE_SEQUENCE,
    payload: request
  };
};

export const showAddModal = () => {
  return {
    type: actionTypes.SHOW_ADD_MODAL
  };
};

export const hideAddModal = () => {
  return {
    type: actionTypes.HIDE_ADD_MODAL
  };
};

export const showDeleteModal = sequenceToDelete => {
  return {
    type: actionTypes.SHOW_DELETE_MODAL,
    payload: { sequenceToDelete }
  };
};

export const hideDeleteModal = () => {
  return {
    type: actionTypes.HIDE_DELETE_MODAL
  };
};
