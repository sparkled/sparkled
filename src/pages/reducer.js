import { combineReducers } from 'redux';
import songListReducer from './SongList/reducer';

export default combineReducers({
  songList: songListReducer
});
