import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchSequence = sequenceId => {
  const request = axios.get(`${restConfig.ROOT_URL}/sequences/${sequenceId}`);

  return {
    type: actionTypes.FETCH_SEQUENCE,
    payload: request
  };
};

export const fetchSequenceStage = sequenceId => {
  const request = axios.get(`${restConfig.ROOT_URL}/sequences/${sequenceId}/stages`);

  return {
    type: actionTypes.FETCH_SEQUENCE_STAGE,
    payload: request
  };
};

export const saveSequence = sequence => {
  const url = `${restConfig.ROOT_URL}/sequences/${sequence.id}`;
  const request = axios.put(url, sequence);

  return {
    type: actionTypes.SAVE_SEQUENCE,
    payload: request
  };
};

export const selectEffect = (selectedChannel, selectedEffect) => {
  return {
    type: actionTypes.SELECT_EFFECT,
    payload: { selectedChannel, selectedEffect }
  };
};

export const updateEffect = (selectedChannel, selectedEffect) => {
  return {
    type: actionTypes.UPDATE_EFFECT,
    payload: { selectedChannel, selectedEffect }
  };
};
