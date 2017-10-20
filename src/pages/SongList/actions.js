import * as actionTypes from './actionTypes';

export const hideDeleteModal = () => {
  return {
    type: actionTypes.HIDE_DELETE_MODAL
  };
};

export const showDeleteModal = songToDelete => {
  return {
    type: actionTypes.SHOW_DELETE_MODAL,
    payload: { songToDelete }
  };
};
