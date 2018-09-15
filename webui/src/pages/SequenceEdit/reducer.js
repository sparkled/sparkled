import _ from 'lodash';
import uuidv4 from 'uuid/v4';
import { getResponseError } from '../../utils/reducerUtils';
import * as actionTypes from './actionTypes';

const initialState = {
  fetchingSequence: false,
  fetchSequenceError: null,
  fetchingStage: false,
  fetchStageError: null,
  saving: null,
  saveError: null,
  sequence: null,
  addChannelModalVisible: false,
  selectedChannel: null,
  selectedEffect: null
};

export default (state = initialState, action) => {
  switch (action.type) {
    case `${actionTypes.FETCH_SEQUENCE}_PENDING`:
      return {
        ...state,
        fetchingSequence: true,
        fetchSequenceError: null,
        selectedChannel: null,
        selectedEffect: null
      };

    case `${actionTypes.FETCH_SEQUENCE}_FULFILLED`:
      const { data } = action.payload;
      let sequence = { ...data, channels: _.map(data.channels, prepareChannel) };
      return {
        ...state,
        fetchingSequence: false,
        sequence
      };

    case `${actionTypes.FETCH_SEQUENCE}_REJECTED`:
      return {
        ...state,
        fetchingSequence: false,
        fetchSequenceError: getResponseError(action)
      };

    case `${actionTypes.FETCH_SEQUENCE_STAGE}_PENDING`:
      return {
        ...state,
        fetchingStage: true,
        fetchStageError: null
      };

    case `${actionTypes.FETCH_SEQUENCE_STAGE}_FULFILLED`:
      const stage = action.payload.data;
      return {
        ...state,
        fetchingStage: false,
        stage: { ...stage },
        stageProps: _.keyBy(stage.stageProps, 'uuid')
      };

    case `${actionTypes.FETCH_SEQUENCE_STAGE}_REJECTED`:
      return {
        ...state,
        fetchingStage: false,
        fetchStageError: getResponseError(action)
      };

    case `${actionTypes.SAVE_SEQUENCE}_PENDING`:
      return {
        ...state,
        saving: true,
        saveError: null
      };

    case `${actionTypes.SAVE_SEQUENCE}_FULFILLED`:
      return {
        ...state,
        saving: false
      };

    case `${actionTypes.SAVE_SEQUENCE}_REJECTED`:
      return {
        ...state,
        saving: false,
        saveError: getResponseError(action)
      };

    case actionTypes.SHOW_ADD_CHANNEL_MODAL:
      return {
        ...state,
        addChannelModalVisible: true
      };

    case actionTypes.HIDE_ADD_CHANNEL_MODAL:
      return {
        ...state,
        addChannelModalVisible: false
      };

    case actionTypes.ADD_CHANNEL:
      return {
        ...state,
        addChannelModalVisible: false,
        sequence: { ...state.sequence, channels: [...state.sequence.channels, action.payload.channel] }
      };

    case actionTypes.SELECT_EFFECT: {
      const { selectedChannel, selectedEffect } = action.payload;
      return { ...state, selectedChannel, selectedEffect };
    }

    case actionTypes.UPDATE_EFFECT: {
      const { selectedChannel, selectedEffect } = action.payload;

      let sequence = { ...state.sequence };
      sequence.channels = _.map(sequence.channels, channel => {
        if (channel.uuid === selectedChannel.uuid) {
          const effects = _.map(channel.effects, effect => {
            return effect.uuid === selectedEffect.uuid ? selectedEffect : effect;
          });
          return { ...channel, effects };
        } else {
          return channel;
        }
      });

      return { ...state, sequence, selectedEffect };
    }

    default:
      return state;
  }
};

function prepareChannel(channel) {
  return { ...channel, effects: _.map(channel.effects, withUuid) };
}

function withUuid(effect) {
  return { ...effect, uuid: uuidv4() };
}
