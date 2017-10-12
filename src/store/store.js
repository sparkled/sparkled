import { createStore, applyMiddleware } from 'redux';
import promiseMiddleware from 'redux-promise-middleware';
import reducer from '../services/reducer';

const createStoreWithMiddleware = applyMiddleware(
  promiseMiddleware()
)(createStore);

export default createStoreWithMiddleware(reducer);
