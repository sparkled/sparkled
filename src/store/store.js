import { createStore, applyMiddleware } from 'redux';
import promise from 'redux-promise';
import reducer from '../services/reducer';

const createStoreWithMiddleware = applyMiddleware(promise)(createStore);
export default createStoreWithMiddleware(reducer);
