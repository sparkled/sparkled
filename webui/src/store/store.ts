import { configureStore, getDefaultMiddleware } from '@reduxjs/toolkit'
import promiseMiddleware from 'redux-promise-middleware'
import rootReducer from './reducers/rootReducer'

const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware().concat(promiseMiddleware()),
})

export default store
