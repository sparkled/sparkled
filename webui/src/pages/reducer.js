import { combineReducers } from 'redux';
import undoable from 'redux-undo';
import schedulerReducer from './Scheduler/reducer';
import sequenceListReducer from './SequenceList/reducer';
import stageEditReducer from './StageEdit/reducer';
import stageListReducer from './StageList/reducer';

const undoableFilter = action => action.undoable === true;

export default combineReducers({
  scheduler: schedulerReducer,
  sequenceList: sequenceListReducer,
  stageEdit: undoable(stageEditReducer, { filter: undoableFilter }),
  stageList: stageListReducer
});
