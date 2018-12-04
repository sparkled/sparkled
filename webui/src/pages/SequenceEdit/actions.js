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
  const request = axios.get(`${restConfig.ROOT_URL}/sequences/${sequenceId}/stage`);

  return {
    type: actionTypes.FETCH_SEQUENCE_STAGE,
    payload: request
  };
};

export const fetchReferenceData = () => {
  const request = axios.all([
    axios.get(`${restConfig.ROOT_URL}/effectTypes`),
    axios.get(`${restConfig.ROOT_URL}/fillTypes`),
    axios.get(`${restConfig.ROOT_URL}/easingTypes`),
  ]);

  return {
    type: actionTypes.FETCH_REFERENCE_DATA,
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

export const addChannel = channel => {
  return {
    type: actionTypes.ADD_CHANNEL,
    undoable: true,
    payload: { channel }
  };
};

export const showAddChannelModal = () => {
  return {
    type: actionTypes.SHOW_ADD_CHANNEL_MODAL
  };
};

export const hideAddChannelModal = () => {
  return {
    type: actionTypes.HIDE_ADD_CHANNEL_MODAL
  };
};

export const selectEffect = (selectedChannel, selectedEffect) => {
  return {
    type: actionTypes.SELECT_EFFECT,
    payload: { selectedChannel, selectedEffect }
  };
};

export const addEffect = effect => {
  return {
    type: actionTypes.ADD_EFFECT,
    undoable: true,
    payload: { effect }
  };
};

export const copyEffect = () => {
  return {
    type: actionTypes.COPY_EFFECT,
    payload: {}
  };
};

export const pasteEffect = () => {
  return {
    type: actionTypes.PASTE_EFFECT,
    payload: {}
  };
};

export const updateEffect = (channel, effect) => {
  return {
    type: actionTypes.UPDATE_EFFECT,
    undoable: true,
    payload: { channel, effect }
  };
};

export const deleteEffect = (channel, effect) => {
  return {
    type: actionTypes.DELETE_EFFECT,
    undoable: true,
    payload: { channel, effect }
  };
};

export const selectFrame = (frame) => {
  return {
    type: actionTypes.SELECT_FRAME,
    payload: { frame }
  };
};
