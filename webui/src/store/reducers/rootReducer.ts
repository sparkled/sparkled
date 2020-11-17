import { combineReducers } from 'redux'
import { reducer as formReducer } from 'redux-form'
import pageReducer from '../../pages/reducer'
import dashboardScreenReducer from './dashboardScreenReducer'

const rootReducer = combineReducers({
  dashboardScreen: dashboardScreenReducer,
  form: formReducer,
  page: pageReducer,
})

export type RootReducerState = ReturnType<typeof rootReducer>
export default rootReducer
