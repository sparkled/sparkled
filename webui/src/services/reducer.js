import { combineReducers } from 'redux';
import scheduledSongReducer from './scheduledSong/reducer';
import songReducer from './song/reducer';
import stageListReducer from './stageList/reducer';

export default combineReducers({
  scheduledSongs: scheduledSongReducer,
  songs: songReducer,
  stageList: stageListReducer
});
