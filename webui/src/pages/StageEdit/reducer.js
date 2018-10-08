import produce from 'immer';
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

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_STAGE_PENDING:
        draft.fetching = true;
        draft.fetchError = null;
        break;

      case actionTypes.FETCH_STAGE_FULFILLED:
        draft.fetching = false;
        draft.stage = action.payload.data;
        draft.selectedStagePropUuid = null;
        break;

      case actionTypes.FETCH_STAGE_REJECTED:
        draft.fetching = false;
        draft.fetchError = getResponseError(action);
        break;

      case actionTypes.SAVE_STAGE_PENDING:
        draft.saving = true;
        draft.saveError = null;
        break;

      case actionTypes.SAVE_STAGE_FULFILLED:
        draft.saving = false;
        break;

      case actionTypes.SAVE_STAGE_REJECTED:
        draft.saving = false;
        draft.saveError = getResponseError(action);
        break;

      case actionTypes.SELECT_STAGE_PROP:
        draft.selectedStagePropUuid = action.payload.uuid;
        break;

      case actionTypes.UPDATE_STAGE_PROP:
        const { stageProp } = action.payload;
        const index = _.findIndex(draft.stage.stageProps, { uuid: stageProp.uuid });
        draft.stage.stageProps[index] = stageProp;
        updateDisplayOrders(draft.stage.stageProps);
        break;

      case actionTypes.ADD_STAGE_PROP:
        draft.stage.stageProps.push(action.payload.stageProp);
        updateDisplayOrders(draft.stage.stageProps);
        break;

      case actionTypes.DELETE_STAGE_PROP:
        draft.stage.stageProps = _.reject(draft.stage.stageProps, { uuid: action.payload.uuid });
        updateDisplayOrders(draft.stage.stageProps);
        break;

      default:
        return state;
    }
  });

  function updateDisplayOrders(stageProps) {
    _.forEach(stageProps, (stageProp, index) => stageProp.displayOrder = index);
  }
};

