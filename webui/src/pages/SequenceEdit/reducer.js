import produce from 'immer';
import _ from 'lodash';
import { getResponseError } from '../../utils/reducerUtils';
import * as actionTypes from './actionTypes';

const initialState = {
  fetchingSequence: false,
  fetchSequenceError: null,
  fetchingStage: false,
  fetchStageError: null,
  fetchingReferenceData: false,
  fetchReferenceDataError: null,
  saving: null,
  saveError: null,
  sequence: null,
  stage: null,
  effectTypes: [],
  fillTypes: [],
  easingTypes: [],
  addChannelModalVisible: false,
  selectedChannel: null,
  selectedEffect: null,
  currentFrame: 0,
  pixelsPerFrame: 2
};

export default (state = initialState, action) => {
  if (!action.type.startsWith(actionTypes.ROOT)) {
    return state;
  }

  return produce(state, draft => {
    switch (action.type) {
      case actionTypes.FETCH_SEQUENCE_PENDING:
        draft.fetchingSequence = true;
        draft.fetchSequenceError = null;
        draft.selectedChannel = null;
        draft.selectedEffect = null;
        draft.currentFrame = 0;
        break;

      case actionTypes.FETCH_SEQUENCE_FULFILLED:
        const sequence = action.payload.data;
        draft.sequence = sequence;
        draft.selectedChannel = sequence.channels.length > 0 ? sequence.channels[0] : null;
        draft.fetchingSequence = false;
        break;

      case actionTypes.FETCH_SEQUENCE_REJECTED:
        draft.fetchingSequence = false;
        draft.fetchSequenceError = getResponseError(action);
        break;

      case actionTypes.FETCH_SEQUENCE_STAGE_PENDING:
        draft.fetchingStage = true;
        draft.fetchStageError = null;
        break;

      case actionTypes.FETCH_SEQUENCE_STAGE_FULFILLED:
        draft.fetchingStage = false;
        draft.stage = action.payload.data;
        break;

      case actionTypes.FETCH_SEQUENCE_STAGE_REJECTED:
        draft.fetchingStage = false;
        draft.fetchStageError = getResponseError(action);
        break;

      case actionTypes.FETCH_REFERENCE_DATA_PENDING:
        draft.fetchingReferenceData = true;
        draft.fetchReferenceDataError = null;
        break;

      case actionTypes.FETCH_REFERENCE_DATA_FULFILLED:
        const [effectTypes, fillTypes, easingTypes] = action.payload;
        draft.fetchingReferenceData = false;
        draft.effectTypes = effectTypes.data;
        draft.fillTypes = fillTypes.data;
        draft.easingTypes = easingTypes.data;
        break;

      case actionTypes.FETCH_REFERENCE_DATA_REJECTED:
        draft.fetchingReferenceData = false;
        draft.fetchReferenceDataError = getResponseError(action);
        break;

      case actionTypes.SAVE_SEQUENCE_PENDING:
        draft.saving = true;
        draft.saveError = null;
        break;

      case actionTypes.SAVE_SEQUENCE_FULFILLED:
        draft.saving = false;
        break;

      case actionTypes.SAVE_SEQUENCE_REJECTED:
        draft.saving = false;
        draft.saveError = getResponseError(action);
        break;

      case actionTypes.SHOW_ADD_CHANNEL_MODAL:
        draft.addChannelModalVisible = true;
        break;

      case actionTypes.HIDE_ADD_CHANNEL_MODAL:
        draft.addChannelModalVisible = false;
        break;

      case actionTypes.ADD_CHANNEL:
        draft.addChannelModalVisible = false;
        draft.sequence.channels.push(action.payload.channel);
        break;

      case actionTypes.SELECT_EFFECT:
        selectEffect(draft, action);
        break;

      case actionTypes.ADD_EFFECT:
        addEffect(draft, action);
        break;

      case actionTypes.UPDATE_EFFECT:
        updateEffect(draft, action);
        break;

      case actionTypes.DELETE_EFFECT:
        deleteEffect(draft, action);
        break;

      case actionTypes.SELECT_FRAME:
        const { frame } = action.payload;
        if (frame >= 0 && frame < state.sequence.durationFrames) {
          draft.currentFrame = action.payload.frame;
        }
        break;

      default:
        return;
    }
  });
};

function hasSameUuid(a, b) {
  return (a || {}).uuid === (b || {}).uuid;
}

function selectEffect(draft, action) {
  const { selectedChannel, selectedEffect } = action.payload;
  const channelSelected = hasSameUuid(selectedChannel, draft.selectedChannel);
  const effectSelected = hasSameUuid(selectedEffect, draft.selectedEffect);

  if (!channelSelected || !effectSelected) {
    draft.selectedChannel = selectedChannel;
    draft.selectedEffect = selectedEffect;
  }
}

// Re-adding the effect has the benefit of automatically moving the effect to the correct array position.
function updateEffect(draft, action) {
  deleteEffect(draft, action);
  addEffect(draft, action);
}

function addEffect(draft, action) {
  const { effect } = action.payload;
  const { channelIndex, effectIndex } = getChannelAndEffectIndexes(draft, action);

  draft.sequence.channels[channelIndex].effects.splice(effectIndex, 0, effect);
  draft.selectedEffect = effect;
}

function deleteEffect(draft, action) {
  const { channelIndex, effectIndex } = getChannelAndEffectIndexes(draft, action);
  draft.sequence.channels[channelIndex].effects.splice(effectIndex, 1);
  draft.selectedEffect = null;
}

function getChannelAndEffectIndexes(draft, action) {
  const { sequence, selectedChannel } = draft;
  const { effect } = action.payload;
  const channelIndex = _.findIndex(sequence.channels, { uuid: selectedChannel.uuid });

  const { effects } = sequence.channels[channelIndex];
  let effectIndex = _.findIndex(effects, { uuid: effect.uuid });
  if (effectIndex === -1) {
    // If effect was not found (i.e. is being added), return the index it should be inserted at.
    effectIndex = _.sortedIndexBy(effects, effect, 'startFrame');
  }

  return { channelIndex, effectIndex };
}
