import axios from 'axios'
import * as restConfig from '../../config/restConfig'
import * as actionTypes from './actionTypes'

export const deletePlaylist = playlistId => {
  const request = axios.delete(`${restConfig.ROOT_URL}/playlists/${playlistId}`)
  return { type: actionTypes.DELETE_PLAYLIST, payload: request }
}

export const playPlaylist = playlistId => {
  const playlistAction = { action: 'PLAY_PLAYLIST', playlistId }
  const request = axios.post(`${restConfig.ROOT_URL}/player`, playlistAction)
  return { type: actionTypes.PLAY_PLAYLIST, payload: request }
}

export const stopPlaylist = () => {
  const playlistAction = { action: 'STOP' }
  const request = axios.post(`${restConfig.ROOT_URL}/player`, playlistAction)
  return { type: actionTypes.STOP_PLAYLIST, payload: request }
}

export const showDeletePlaylistModal = playlistToDelete => ({
  type: actionTypes.SHOW_DELETE_MODAL,
  payload: { playlistToDelete },
})

export const hideDeleteModal = () => ({ type: actionTypes.HIDE_DELETE_MODAL })
