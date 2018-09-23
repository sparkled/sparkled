import _ from 'lodash';
import * as actionTypes from './actionTypes';
import { getResponseError } from '../../utils/reducerUtils';

const initialState = {
  fetching: false,
  fetchError: null,
  saving: null,
  saveError: null,
  stage: null,
  selectedStagePropUuid: null
};

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  switch (action.type) {
    case actionTypes.FETCH_STAGE_PENDING:
      return { ...state, fetching: true, fetchError: null };

    case actionTypes.FETCH_STAGE_FULFILLED:
      return { ...state, fetching: false, stage: { ...action.payload.data }, selectedStagePropUuid: null };

    case actionTypes.FETCH_STAGE_REJECTED:
      return { ...state, fetching: false, fetchError: getResponseError(action) };

    case actionTypes.SAVE_STAGE_PENDING:
      return { ...state, saving: true, saveError: null };

    case actionTypes.SAVE_STAGE_FULFILLED:
      return { ...state, saving: false };

    case actionTypes.SAVE_STAGE_REJECTED:
      return { ...state, saving: false, saveError: getResponseError(action) };

    case actionTypes.SELECT_STAGE_PROP:
      return { ...state, selectedStagePropUuid: action.payload.uuid };

    case actionTypes.UPDATE_STAGE_PROP:
      const updatedStageProp = action.payload.stageProp;
      return {
        ...state,
        stage: {
          ...state.stage,
          stageProps: _.map(state.stage.stageProps, stageProp => {
            return updatedStageProp.uuid === stageProp.uuid ? updatedStageProp : stageProp;
          })
        }
      };

    case actionTypes.ADD_STAGE_PROP:
      return {
        ...state,
        stage: {
          ...state.stage,
          stageProps: [...state.stage.stageProps, action.payload.stageProp]
        }
      };

    case actionTypes.DELETE_STAGE_PROP:
      // TODO: Update stage prop display orders
      const stage = state.stage;
      const { uuid } = action.payload;

      return {
        ...state,
        stage: {
          ...stage,
          stageProps: _.reject(stage.stageProps, { uuid }),
        }
      };

    default:
      return state;
  }
};

