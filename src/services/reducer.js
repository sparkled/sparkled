import { combineReducers } from 'redux';
import songReducer from './song/reducer';

export default combineReducers({
  songs: songReducer
});
