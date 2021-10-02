import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { addPlaylist, deletePlaylist } from './dashboardScreenReducer'

export type ModalStatus = 'closed' | 'open' | 'loading'

export type ModalState = {
  status: ModalStatus
  data?: any
  error?: string
}

export type State = {
  modals: Record<ModalName, ModalState>
}

export type ModalName =
  | 'playlistAdd'
  | 'playlistDelete'
  | 'scheduledTaskAdd'
  | 'scheduledTaskDelete'
  | 'sequenceAdd'
  | 'sequenceDelete'
  | 'songAdd'
  | 'songDelete'
  | 'stageAdd'
  | 'stageDelete'

const initialState: State = {
  modals: {
    playlistAdd: { status: 'closed' },
    playlistDelete: { status: 'closed' },
    scheduledTaskAdd: { status: 'closed' },
    scheduledTaskDelete: { status: 'closed' },
    sequenceAdd: { status: 'closed' },
    sequenceDelete: { status: 'closed' },
    songAdd: { status: 'closed' },
    songDelete: { status: 'closed' },
    stageAdd: { status: 'closed' },
    stageDelete: { status: 'closed' }
  }
}

const slice = createSlice({
  name: 'dashboardScreen',
  initialState,
  reducers: {
    setModalStatus(state, action: PayloadAction<{ modal: ModalName } & ModalState>) {
      state.modals[action.payload.modal].status = action.payload.status
      state.modals[action.payload.modal].error = action.payload.error
      state.modals[action.payload.modal].data = action.payload.data
    }
  },

  extraReducers(builder) {
    builder.addCase(addPlaylist.pending, state => {
      state.modals['playlistAdd'].status = 'loading'
      state.modals['playlistAdd'].error = undefined
    })

    builder.addCase(addPlaylist.fulfilled, state => {
      state.modals['playlistAdd'].status = 'closed'
      state.modals['playlistAdd'].error = undefined
    })

    builder.addCase(addPlaylist.rejected, (state, action) => {
      state.modals['playlistAdd'].status = 'open'
      state.modals['playlistAdd'].error = action.error.message as string
    })

    builder.addCase(deletePlaylist.pending, state => {
      state.modals['playlistDelete'].status = 'loading'
      state.modals['playlistDelete'].error = undefined
    })

    builder.addCase(deletePlaylist.fulfilled, state => {
      state.modals['playlistDelete'].status = 'closed'
      state.modals['playlistDelete'].error = undefined
    })

    builder.addCase(deletePlaylist.rejected, (state, action) => {
      state.modals['playlistDelete'].status = 'open'
      state.modals['playlistDelete'].error = action.error.message as string
    })
  }
})

export const { setModalStatus } = slice.actions
export default slice.reducer
