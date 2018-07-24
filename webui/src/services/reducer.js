import { combineReducers } from 'redux';
import scheduledSongReducer from './scheduledSong/reducer';
import songReducer from './song/reducer';
import stageReducer from './stage/reducer';

export default combineReducers({
  scheduledSongs: scheduledSongReducer,
  songs: songReducer,
  stages: stageReducer
});
