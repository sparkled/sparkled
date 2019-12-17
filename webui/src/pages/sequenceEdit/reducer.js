import produce from 'immer'
import _ from 'lodash'
import uuidv4 from 'uuid/v4'
import { getResponseError } from '../../utils/reducerUtils'
import * as actionTypes from './actionTypes'

const initialState = {
  fetchingSequence: false,
  fetchSequenceError: null,
  fetchingStage: false,
  fetchStageError: null,
  fetchingReferenceData: false,
  fetchReferenceDataError: null,
  fetchingRenderData: false,
  fetchRenderDataError: null,
  saving: null,
  saveError: null,
  sequence: null,
  stage: null,
  renderData: null,
  effectTypes: [],
  fillTypes: [],
  easingTypes: [],
  addChannelModalVisible: false,
  selectedChannel: null,
  selectedEffect: null,
  currentFrame: 0,
  playbackFrame: null,
  playbackSpeed: 100,
  previewDuration: 5,
  pixelsPerFrame: 2,
  copiedEffect: null
}

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state
  }

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_SEQUENCE_PENDING:
        draft.fetchingSequence = true
        draft.fetchSequenceError = null
        draft.selectedChannel = null
        draft.selectedEffect = null
        draft.currentFrame = 0
        break

      case actionTypes.FETCH_SEQUENCE_FULFILLED:
        draft.fetchingSequence = false

        const sequence = action.payload.data
        draft.sequence = sequence
        draft.selectedChannel =
          sequence.channels.length > 0 ? sequence.channels[0] : null
        break

      case actionTypes.FETCH_SEQUENCE_REJECTED:
        draft.fetchingSequence = false
        draft.fetchSequenceError = getResponseError(action)
        break

      case actionTypes.FETCH_SEQUENCE_STAGE_PENDING:
        draft.fetchingStage = true
        draft.fetchStageError = null
        break

      case actionTypes.FETCH_SEQUENCE_STAGE_FULFILLED:
        draft.fetchingStage = false
        draft.stage = action.payload.data
        break

      case actionTypes.FETCH_SEQUENCE_STAGE_REJECTED:
        draft.fetchingStage = false
        draft.fetchStageError = getResponseError(action)
        break

      case actionTypes.FETCH_REFERENCE_DATA_PENDING:
        draft.fetchingReferenceData = true
        draft.fetchReferenceDataError = null
        break

      case actionTypes.FETCH_REFERENCE_DATA_FULFILLED:
        const [effectTypes, fillTypes, easingTypes] = action.payload
        draft.fetchingReferenceData = false
        draft.effectTypes = _.mapKeys(effectTypes.data, 'code')
        draft.fillTypes = _.mapKeys(fillTypes.data, 'code')
        draft.easingTypes = _.mapKeys(easingTypes.data, 'code')
        break

      case actionTypes.FETCH_REFERENCE_DATA_REJECTED:
        draft.fetchingReferenceData = false
        draft.fetchReferenceDataError = getResponseError(action)
        break

      case actionTypes.SAVE_SEQUENCE_PENDING:
        draft.saving = true
        draft.saveError = null
        break

      case actionTypes.SAVE_SEQUENCE_FULFILLED:
        draft.saving = false
        break

      case actionTypes.SAVE_SEQUENCE_REJECTED:
        draft.saving = false
        draft.saveError = getResponseError(action)
        break

      case actionTypes.SHOW_ADD_CHANNEL_MODAL:
        draft.addChannelModalVisible = true
        break

      case actionTypes.HIDE_ADD_CHANNEL_MODAL:
        draft.addChannelModalVisible = false
        break

      case actionTypes.ADD_CHANNEL:
        draft.addChannelModalVisible = false

        draft.sequence.channels.forEach(it => {
          if (it.displayOrder >= action.payload.channel.displayOrder) {
            it.displayOrder++
          }
        })

        draft.sequence.channels.push(action.payload.channel)
        draft.sequence.channels = _.sortBy(
          draft.sequence.channels,
          'displayOrder'
        )
        break

      case actionTypes.SELECT_EFFECT:
        selectEffect(draft, action)
        break

      case actionTypes.ADD_EFFECT:
        addEffect(draft, action.payload.effect)
        break

      case actionTypes.COPY_EFFECT:
        draft.copiedEffect = state.selectedEffect
        break

      case actionTypes.PASTE_EFFECT:
        if (state.copiedEffect) {
          const frameOffset = state.currentFrame - state.copiedEffect.startFrame
          const effect = {
            ...state.copiedEffect,
            uuid: uuidv4(),
            startFrame: state.currentFrame,
            endFrame: state.copiedEffect.endFrame + frameOffset
          }

          addEffect(draft, effect)
        }
        break

      case actionTypes.CANCEL_RENDER:
        if (draft.renderData !== null) {
          draft.fetchingRenderData = false
          draft.renderData = null
        }
        break

      case actionTypes.ADJUST_PREVIEW_DURATION:
        draft.previewDuration = action.payload.previewDuration
        break

      case actionTypes.ADJUST_PLAYBACK_SPEED:
        draft.playbackSpeed = action.payload.playbackSpeed
        break

      case actionTypes.FETCH_RENDER_PREVIEW_DATA_PENDING:
        draft.fetchingRenderData = true
        draft.renderData = null
        draft.fetchRenderDataError = null
        break

      case actionTypes.FETCH_RENDER_PREVIEW_DATA_FULFILLED:
        draft.fetchingRenderData = false
        draft.renderData = action.payload.data
        break

      case actionTypes.FETCH_RENDER_PREVIEW_DATA_REJECTED:
        draft.fetchingRenderData = false
        draft.fetchRenderDataError = getResponseError(action)
        break

      case actionTypes.UPDATE_EFFECT:
        updateEffect(draft, action.payload.effect)
        break

      case actionTypes.DELETE_EFFECT:
        deleteEffect(draft, action.payload.effect)
        break

      case actionTypes.SELECT_FRAME:
        const { frame } = action.payload
        if (frame >= 0 && frame < state.sequence.frameCount) {
          draft.currentFrame = frame
        }
        break

      default:
        return
    }
  })
}

function hasSameUuid(a, b) {
  return (a || {}).uuid === (b || {}).uuid
}

function selectEffect(draft, action) {
  const { selectedChannel, selectedEffect } = action.payload
  const channelSelected = hasSameUuid(selectedChannel, draft.selectedChannel)
  const effectSelected = hasSameUuid(selectedEffect, draft.selectedEffect)

  if (!channelSelected || !effectSelected) {
    draft.selectedChannel = selectedChannel
    draft.selectedEffect = selectedEffect
  }
}

// Re-adding the effect has the benefit of automatically moving the effect to the correct array position.
function updateEffect(draft, effect) {
  deleteEffect(draft, effect)
  addEffect(draft, effect)
}

function addEffect(draft, effect) {
  const { channelIndex, effectIndex } = getChannelAndEffectIndexes(
    draft,
    effect
  )
  draft.sequence.channels[channelIndex].effects.splice(effectIndex, 0, effect)
  draft.selectedEffect = effect
}

function deleteEffect(draft, effect) {
  const { channelIndex, effectIndex } = getChannelAndEffectIndexes(
    draft,
    effect
  )
  draft.sequence.channels[channelIndex].effects.splice(effectIndex, 1)
  draft.selectedEffect = null
}

function getChannelAndEffectIndexes(draft, effect) {
  const { sequence, selectedChannel } = draft
  const channelIndex = _.findIndex(sequence.channels, {
    uuid: selectedChannel.uuid
  })

  const { effects } = sequence.channels[channelIndex]
  let effectIndex = _.findIndex(effects, { uuid: effect.uuid })
  if (effectIndex === -1) {
    // If effect was not found (i.e. is being added), return the index it should be inserted at.
    effectIndex = _.sortedIndexBy(effects, effect, 'startFrame')
  }

  return { channelIndex, effectIndex }
}
