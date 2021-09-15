import { configureStore, getDefaultMiddleware } from '@reduxjs/toolkit'
import promiseMiddleware from 'redux-promise-middleware'
import rootReducer from './reducers/rootReducer'

const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware({
    immutableCheck: false,
    serializableCheck: false,
  }).concat(promiseMiddleware()),
})

export default store
