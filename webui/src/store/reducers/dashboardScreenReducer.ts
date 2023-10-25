import { AnyAction, createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit'
import axios from 'axios'
import * as restConfig from '../../config/restConfig'
import { DashboardViewModel, PlaylistViewModel, RequestStatus } from '../../hooks/api/apiTypes'

export type State = {
  requestStatus: RequestStatus
  dashboard: DashboardViewModel | null
  query: string
}

const initialState: State = {
  requestStatus: 'loading',
  dashboard: null,
  query: ''
}

export const addPlaylist = createAsyncThunk('dashboard/addPlaylist', async (playlist: PlaylistViewModel) => {
  const url = `${restConfig.ROOT_URL}/playlists`
  return (await axios.post(url, playlist)).data as PlaylistViewModel
})

export const deletePlaylist = createAsyncThunk('dashboard/deletePlaylist', async (playlist: PlaylistViewModel) => {
  const url = `${restConfig.ROOT_URL}/playlists/${playlist.id}`
  await axios.delete(url)
  return playlist
})

const slice = createSlice({
  name: 'dashboardScreen',
  initialState,
  reducers: {
    reload(state) {
      state.requestStatus = 'reloading'
    },
    loading(state) {
      state.requestStatus = 'loading'
    },
    loaded(state, action: PayloadAction<DashboardViewModel>) {
      state.requestStatus = 'success'
      state.dashboard = action.payload
    },
    failed(state) {
      state.requestStatus = 'fail'
    },
    setQuery(state, action: PayloadAction<string>) {
      state.query = action.payload
    }
  },
  extraReducers(builder) {
    builder.addCase(addPlaylist.fulfilled, (state, action) => {
      if (state.dashboard) {
        state.dashboard.playlists.push({
          id: action.payload.id,
          name: action.payload.name,
          durationMs: 0,
          sequenceCount: 0
        })
      }
    })

    builder.addCase(deletePlaylist.fulfilled, (state, action) => {
      if (state.dashboard) {
        state.dashboard.playlists = state.dashboard.playlists.filter(it => it.id !== action.payload.id)
      }
    })

    // TODO this is a workaround to refresh the Dashboard screen whenever any API action completes.
    function isActionWithNumberPayload(action: AnyAction): action is AnyAction {
      return action.type.endsWith('FULFILLED')
    }

    builder.addMatcher(isActionWithNumberPayload, state => {
      state.requestStatus = 'reloading'
    })
  }
})

export const {
  loading, loaded, failed, setQuery
} = slice.actions
export default slice.reducer
