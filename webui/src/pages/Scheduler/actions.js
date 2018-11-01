import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchScheduledJobs = () => {
  const request = axios.get(`${restConfig.ROOT_URL}/scheduledJobs`);
  return { type: actionTypes.FETCH_SCHEDULED_JOBS, payload: request };
};

export const addScheduledJob = scheduledJob => {
  const url = `${restConfig.ROOT_URL}/scheduledJobs`;
  const request = axios.post(url, scheduledJob);
  return { type: actionTypes.ADD_SCHEDULED_JOB, payload: request };
};

export const deleteScheduledJob = scheduledJobId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/scheduledJobs/${scheduledJobId}`);
  return { type: actionTypes.DELETE_SCHEDULED_JOB, payload: request };
};

export const showAddModal = () => ({ type: actionTypes.SHOW_ADD_MODAL });

export const hideAddModal = () => ({ type: actionTypes.HIDE_ADD_MODAL });

export const showDeleteModal = scheduledJobToDelete => ({
  type: actionTypes.SHOW_DELETE_MODAL,
  payload: { scheduledJobToDelete }
});

export const hideDeleteModal = () => ({ type: actionTypes.HIDE_DELETE_MODAL });
