import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchScheduledSequences = date => {
  const request = axios.get(`${restConfig.ROOT_URL}/scheduledSequences`, { params: { date } });

  return {
    type: actionTypes.FETCH_SCHEDULED_SEQUENCES,
    payload: request
  };
};

export const addScheduledSequence = scheduledSequence => {
  const url = `${restConfig.ROOT_URL}/scheduledSequences`;
  const request = axios.post(url, scheduledSequence);

  return {
    type: actionTypes.ADD_SCHEDULED_SEQUENCE,
    payload: request
  };
};

export const deleteScheduledSequence = scheduledSequenceId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/scheduledSequences/${scheduledSequenceId}`);

  return {
    type: actionTypes.DELETE_SCHEDULED_SEQUENCE,
    payload: request
  };
};
