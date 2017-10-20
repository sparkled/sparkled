import { combineReducers, createStore, applyMiddleware } from 'redux';
import promiseMiddleware from 'redux-promise-middleware';
import pageReducer from '../pages/reducer';
import serviceReducer from '../services/reducer';

const reducer = combineReducers({
  page: pageReducer,
  data: serviceReducer
});

const createStoreWithMiddleware = applyMiddleware(
  promiseMiddleware()
)(createStore);

export default createStoreWithMiddleware(reducer);
