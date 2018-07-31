import axios from 'axios';
import * as restConfig from '../../config/restConfig';
import * as actionTypes from './actionTypes';

export const fetchStage = stageId => {
  const request = axios.get(`${restConfig.ROOT_URL}/stages/${stageId}`);

  return {
    type: actionTypes.FETCH_STAGE,
    payload: request
  };
};

export const saveStage = stage => {
  const url = `${restConfig.ROOT_URL}/stages/${stage.id}`;
  const request = axios.put(url, stage);

  return {
    type: actionTypes.SAVE_STAGE,
    payload: request
  };
};

export const addStageProp = stageProp => {
  return {
    type: actionTypes.ADD_STAGE_PROP,
    payload: { undoable: true, stageProp }
  };
};

export const updateStageProp = stageProp => {
  return {
    type: actionTypes.UPDATE_STAGE_PROP,
    payload: { undoable: true, stageProp }
  };
};

export const deleteStageProp = uuid => {
  return {
    type: actionTypes.DELETE_STAGE_PROP,
    payload: { undoable: true, uuid }
  };
};

export const selectStageProp = uuid => {
  return {
    type: actionTypes.SELECT_STAGE_PROP,
    payload: { undoable: true, uuid }
  };
};
