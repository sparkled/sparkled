import { combineReducers } from 'redux';
import schedulerReducer from './Scheduler/reducer';
import songListReducer from './SongList/reducer';
import stageListReducer from './StageList/reducer';

export default combineReducers({
  scheduler: schedulerReducer,
  songList: songListReducer,
  stageList: stageListReducer
});
