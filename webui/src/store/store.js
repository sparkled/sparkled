import { combineReducers, createStore, applyMiddleware } from 'redux';
import { reducer as formReducer } from 'redux-form';
import promiseMiddleware from 'redux-promise-middleware';
import pageReducer from '../pages/reducer';

const reducer = combineReducers({
  form: formReducer,
  page: pageReducer
});

const createStoreWithMiddleware = applyMiddleware(
  promiseMiddleware()
)(createStore);

export default createStoreWithMiddleware(reducer);
