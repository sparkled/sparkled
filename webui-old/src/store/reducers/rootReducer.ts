import { useDispatch } from 'react-redux'
import { combineReducers } from 'redux'
import { reducer as formReducer } from 'redux-form'
import pageReducer from '../../pages/reducer'
import { AppDispatch } from '../store'
import dashboardScreenReducer from './dashboardScreenReducer'
import modalReducer from './modalReducer'

const rootReducer = combineReducers({
  dashboardScreen: dashboardScreenReducer,
  form: formReducer,
  modal: modalReducer,
  page: pageReducer
})

export const useAppDispatch = () => useDispatch<AppDispatch>()
export default rootReducer
