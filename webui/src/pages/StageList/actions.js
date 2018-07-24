import * as actionTypes from './actionTypes';

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

export const showDeleteModal = stageToDelete => {
  return {
    type: actionTypes.SHOW_DELETE_MODAL,
    payload: { stageToDelete }
  };
};

export const hideDeleteModal = () => {
  return {
    type: actionTypes.HIDE_DELETE_MODAL
  };
};
