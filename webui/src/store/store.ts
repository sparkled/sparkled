import { configureStore } from '@reduxjs/toolkit'
import { TypedUseSelectorHook, useSelector } from 'react-redux'
import promiseMiddleware from 'redux-promise-middleware'
import rootReducer from './reducers/rootReducer'

const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware => getDefaultMiddleware({
    immutableCheck: false,
    serializableCheck: false,
  }).concat(promiseMiddleware()),
})

export type AppState = ReturnType<typeof store.getState>
export const useAppSelector: TypedUseSelectorHook<AppState> = useSelector
export type AppDispatch = typeof store.dispatch
export default store
