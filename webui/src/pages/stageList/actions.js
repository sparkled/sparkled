import axios from 'axios'
import * as restConfig from '../../config/restConfig'
import * as actionTypes from './actionTypes'

export const fetchStages = () => {
  const request = axios.get(`${restConfig.ROOT_URL}/stages`)

  return {
    type: actionTypes.FETCH_STAGES,
    payload: request,
  }
}

export const addStage = stage => {
  const url = `${restConfig.ROOT_URL}/stages`
  const request = axios.post(url, stage)

  return {
    type: actionTypes.ADD_STAGE,
    payload: request,
  }
}

export const deleteStage = stageId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/stages/${stageId}`)

  return {
    type: actionTypes.DELETE_STAGE,
    payload: request,
  }
}

export const showAddStageModal = () => {
  return {
    type: actionTypes.SHOW_ADD_MODAL,
  }
}

export const hideAddStageModal = () => {
  return {
    type: actionTypes.HIDE_ADD_MODAL,
  }
}

export const showDeleteStageModal = stageToDelete => {
  return {
    type: actionTypes.SHOW_DELETE_MODAL,
    payload: { stageToDelete },
  }
}

export const hideDeleteModal = () => {
  return {
    type: actionTypes.HIDE_DELETE_MODAL,
  }
}
