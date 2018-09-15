import _ from 'lodash';
import immutable from 'object-path-immutable';
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
  switch (action.type) {
    case `${actionTypes.FETCH_SEQUENCE}_PENDING`:
      return {
        ...state,
        fetchingSequence: true, fetchSequenceError: null, selectedChannel: null, selectedEffect: null
      };

    case `${actionTypes.FETCH_SEQUENCE}_FULFILLED`:
      const sequence = action.payload.data;
      const selectedChannel = sequence.channels.length > 0 ? sequence.channels[0] : null;
      return { ...state, sequence, selectedChannel, fetchingSequence: false };

    case `${actionTypes.FETCH_SEQUENCE}_REJECTED`:
      return { ...state, fetchingSequence: false, fetchSequenceError: getResponseError(action) };

    case `${actionTypes.FETCH_SEQUENCE_STAGE}_PENDING`:
      return { ...state, fetchingStage: true, fetchStageError: null };

    case `${actionTypes.FETCH_SEQUENCE_STAGE}_FULFILLED`:
      return { ...state, fetchingStage: false, stage: action.payload.data };

    case `${actionTypes.FETCH_SEQUENCE_STAGE}_REJECTED`:
      return { ...state, fetchingStage: false, fetchStageError: getResponseError(action) };

    case `${actionTypes.FETCH_REFERENCE_DATA}_PENDING`:
      return { ...state, fetchingReferenceData: true, fetchReferenceDataError: null };

    case `${actionTypes.FETCH_REFERENCE_DATA}_FULFILLED`:
      const [effectTypes, fillTypes, easingTypes] = action.payload;
      return {
        ...state, fetchingReferenceData: false,
        effectTypes: effectTypes.data, fillTypes: fillTypes.data, easingTypes: easingTypes.data
      };

    case `${actionTypes.FETCH_REFERENCE_DATA}_REJECTED`:
      return { ...state, fetchingReferenceData: false, fetchReferenceDataError: getResponseError(action) };

    case `${actionTypes.SAVE_SEQUENCE}_PENDING`:
      return { ...state, saving: true, saveError: null };

    case `${actionTypes.SAVE_SEQUENCE}_FULFILLED`:
      return { ...state, saving: false };

    case `${actionTypes.SAVE_SEQUENCE}_REJECTED`:
      return { ...state, saving: false, saveError: getResponseError(action) };

    case actionTypes.SHOW_ADD_CHANNEL_MODAL:
      return { ...state, addChannelModalVisible: true };

    case actionTypes.HIDE_ADD_CHANNEL_MODAL:
      return { ...state, addChannelModalVisible: false };

    case actionTypes.ADD_CHANNEL:
      return {
        ...state, addChannelModalVisible: false,
        sequence: { ...state.sequence, channels: [...state.sequence.channels, action.payload.channel] }
      };

    case actionTypes.SELECT_EFFECT: {
      const { selectedChannel, selectedEffect } = action.payload;

      if (hasSameUuid(selectedChannel, state.selectedChannel) && hasSameUuid(selectedEffect, state.selectedEffect)) {
        return state;
      } else {
        return { ...state, selectedChannel, selectedEffect };
      }
    }

    case actionTypes.ADD_EFFECT: {
      return addEffect(state, action);
    }

    case actionTypes.UPDATE_EFFECT: {
      return updateEffect(state, action);
    }

    case actionTypes.DELETE_EFFECT: {
      return deleteEffect(state, action);
    }

    case actionTypes.SELECT_FRAME: {
      const { frame } = action.payload;
      if (frame < 0 || frame > state.sequence.durationFrames - 1) {
        return state; // Frame is out of bounds, ignore.
      } else {
        return { ...state, currentFrame: action.payload.frame };
      }
    }

    default:
      return state;
  }
};

function hasSameUuid(a, b) {
  return (a || {}).uuid === (b || {}).uuid;
}

// Re-adding the effect has the benefit of automatically moving the effect to the correct array position.
function updateEffect(state, action) {
  state = deleteEffect(state, action);
  return addEffect(state, action);
}

function addEffect(state, action) {
  const { effect } = action.payload;
  const { channelIndex, effectIndex } = getChannelAndEffectIndexes(state, action);
  const effectPath = `channels.${channelIndex}.effects`;

  return {
    ...state,
    sequence: immutable.insert(state.sequence, effectPath, effect, effectIndex),
    selectedEffect: effect
  };
}

function deleteEffect(state, action) {
  const { channelIndex, effectIndex } = getChannelAndEffectIndexes(state, action);
  const effectPath = `channels.${channelIndex}.effects.${effectIndex}`;
  return {
    ...state,
    sequence: immutable.del(state.sequence, effectPath),
    selectedEffect: null
  };
}

function getChannelAndEffectIndexes(state, action) {
  const { sequence, selectedChannel } = state;
  const { effect } = action.payload;
  const channelIndex = _.findIndex(sequence.channels, { uuid: selectedChannel.uuid });

  // Finding the effect by sorted startFrame index returns the correct deletion index for existing effects and the
  // correct insertion index for new effects.
  const effectIndex = _.sortedIndexBy(sequence.channels[channelIndex].effects, effect, 'startFrame');
  return { channelIndex, effectIndex };
}
