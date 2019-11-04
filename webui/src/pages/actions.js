import axios from 'axios'
import * as restConfig from '../config/restConfig'
import * as actionTypes from './actionTypes'

export const setCurrentPage = ({ pageTitle, pageClass }) => {
  return {
    type: actionTypes.SET_CURRENT_PAGE,
    payload: { pageTitle, pageClass }
  }
}

export const fetchBrightness = () => {
  const request = axios.get(`${restConfig.ROOT_URL}/settings/BRIGHTNESS`)
  return { type: actionTypes.FETCH_BRIGHTNESS, payload: request }
}

export const updateBrightness = brightness => {
  const request = axios.put(`${restConfig.ROOT_URL}/settings/BRIGHTNESS`, {
    code: 'BRIGHTNESS',
    value: brightness
  })
  return { type: actionTypes.UPDATE_BRIGHTNESS, payload: request }
}
