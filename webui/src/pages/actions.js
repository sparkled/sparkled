import * as actionTypes from './actionTypes';

export const setCurrentPage = ({ pageTitle, pageClass }) => {
  return {
    type: actionTypes.SET_CURRENT_PAGE,
    payload: { pageTitle, pageClass }
  };
};
