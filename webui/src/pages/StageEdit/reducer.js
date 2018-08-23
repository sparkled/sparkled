import _ from 'lodash';
import uuidv4 from 'uuid/v4';
import * as actionTypes from './actionTypes';

const initialState = {
  fetching: false,
  fetchError: null,
  saving: null,
  saveError: null,
  stage: null,
  selectedStagePropUuid: null
};

export default (state = initialState, action) => {
  switch (action.type) {

    case `${actionTypes.FETCH_STAGE}_PENDING`:
      return {
        ...state,
        fetching: true,
        fetchError: null
      };

    case `${actionTypes.FETCH_STAGE}_FULFILLED`:
      return {
        ...state,
        fetching: false,
        stage: { ...action.payload.data, stageProps: withUuids(action.payload.data.stageProps) },
        selectedStagePropUuid: null
      };

    case `${actionTypes.FETCH_STAGE}_REJECTED`:
      return {
        ...state,
        fetching: false,
        fetchError: action.payload.response.data,
      };

    case `${actionTypes.SAVE_STAGE}_PENDING`:
      return {
        ...state,
        saving: true,
        saveError: null
      };

    case `${actionTypes.SAVE_STAGE}_FULFILLED`:
      return {
        ...state,
        saving: false
      };

    case `${actionTypes.SAVE_STAGE}_REJECTED`:
      return {
        ...state,
        saving: false,
        saveError: action.payload.response.data
      };

    case actionTypes.SELECT_STAGE_PROP:
      return {
        ...state,
        selectedStagePropUuid: action.payload.uuid
      };

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
          stageProps: [...state.stage.stageProps, withUuid(action.payload.stageProp)]
        }
      };

    case actionTypes.DELETE_STAGE_PROP:
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

function withUuids(stageProps) {
  return _.map(stageProps, withUuid);
}

function withUuid(stageProp) {
  return { ...stageProp, uuid: uuidv4() };
}
