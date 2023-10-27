import axios from 'axios'
import * as restConfig from '../../config/restConfig'
import * as actionTypes from './actionTypes'

export const fetchPlaylist = playlistId => {
  const request = axios.get(`${restConfig.ROOT_URL}/playlists/${playlistId}`)
  return { type: actionTypes.FETCH_PLAYLIST, payload: request }
}

export const fetchSequences = () => {
  const request = axios.get(`${restConfig.ROOT_URL}/sequences`)
  return { type: actionTypes.FETCH_SEQUENCES, payload: request }
}

export const savePlaylist = playlist => {
  const request = axios.put(`${restConfig.ROOT_URL}/playlists/${playlist.id}`, playlist)
  return { type: actionTypes.SAVE_PLAYLIST, payload: request }
}

export const addSequence = sequence => {
  return {
    type: actionTypes.ADD_SEQUENCE,
    undoable: true,
    payload: { sequence },
  }
}

export const showAddSequenceModal = () => {
  return {
    type: actionTypes.SHOW_ADD_SEQUENCE_MODAL,
  }
}

export const hideAddSequenceModal = () => {
  return {
    type: actionTypes.HIDE_ADD_SEQUENCE_MODAL,
  }
}

export const deletePlaylistSequence = id => {
  return {
    type: actionTypes.DELETE_PLAYLIST_SEQUENCE,
    undoable: true,
    payload: { uuid },
  }
}

export const updatePlaylistSequence = playlistSequence => {
  return {
    type: actionTypes.UPDATE_PLAYLIST_SEQUENCE,
    undoable: true,
    payload: { playlistSequence },
  }
}
