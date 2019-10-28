import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchSequences = () => {
  const request = axios.get(`${restConfig.ROOT_URL}/sequences`);
  return { type: actionTypes.FETCH_SEQUENCES, payload: request };
};

export const addSequence = sequence => {
  const url = `${restConfig.ROOT_URL}/sequences`;
  const request = axios.post(url, sequence);

  return {
    type: actionTypes.ADD_SEQUENCE,
    payload: request
  };
};

export const deleteSequence = sequenceId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/sequences/${sequenceId}`);
  return { type: actionTypes.DELETE_SEQUENCE, payload: request };
};

export const showAddModal = () => ({ type: actionTypes.SHOW_ADD_MODAL });

export const hideAddModal = () => ({ type: actionTypes.HIDE_ADD_MODAL });

export const showDeleteModal = sequenceToDelete => ({
  type: actionTypes.SHOW_DELETE_MODAL,
  payload: { sequenceToDelete }
});

export const hideDeleteModal = () => ({ type: actionTypes.HIDE_DELETE_MODAL });
