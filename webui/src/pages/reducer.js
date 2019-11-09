import { combineReducers } from 'redux'
import undoable from 'redux-undo'
import * as actionTypes from './actionTypes'
import playlistEditReducer from './playlistEdit/reducer'
import playlistListReducer from './playlistList/reducer'
import schedulerReducer from './scheduler/reducer'
import sequenceEditReducer from './sequenceEdit/reducer'
import sequenceListReducer from './sequenceList/reducer'
import songListReducer from './songList/reducer'
import stageListReducer from './stageList/reducer'

const undoableFilter = action => action.undoable === true

const initialState = {
  brightness: null
}

const sharedReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.SET_CURRENT_PAGE:
      const { pageTitle, pageClass } = action.payload
      return { ...state, pageTitle, pageClass }

    case actionTypes.FETCH_BRIGHTNESS_PENDING:
      return { ...state, brightness: null }

    case actionTypes.FETCH_BRIGHTNESS_FULFILLED:
      return { ...state, brightness: Number(action.payload.data.value) }

    default:
      return state
  }
}

export default combineReducers({
  shared: sharedReducer,
  stageList: stageListReducer,
  songList: songListReducer,
  sequenceEdit: undoable(sequenceEditReducer, { filter: undoableFilter }),
  sequenceList: sequenceListReducer,
  playlistEdit: undoable(playlistEditReducer, { filter: undoableFilter }),
  playlistList: playlistListReducer,
  scheduler: schedulerReducer
})
