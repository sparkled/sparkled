import { combineReducers, createStore, applyMiddleware } from 'redux';
import { reducer as formReducer } from 'redux-form';
import promiseMiddleware from 'redux-promise-middleware';
import pageReducer from '../pages/reducer';
import serviceReducer from '../services/reducer';

const reducer = combineReducers({
  form: formReducer,
  page: pageReducer,
  data: serviceReducer
});

const createStoreWithMiddleware = applyMiddleware(
  promiseMiddleware()
)(createStore);

export default createStoreWithMiddleware(reducer);
