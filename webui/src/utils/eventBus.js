import _ from 'lodash';

const subscribers = {};

export const eventType = {
  RENDER_DATA: 'RENDER_DATA'
};

export const subscribe = (eventType, key, callback) => {
  if (!subscribers[eventType]) {
    subscribers[eventType] = {};
  }

  subscribers[eventType][key] = callback;
};

export const unsubscribe = (eventType, key) => {
  if (subscribers[eventType]) {
    delete subscribers[eventType][key];
  }
};

export const publish = (eventType, data) => {
  if (subscribers[eventType]) {
    _(subscribers[eventType]).values().forEach(callback => callback(data));
  }
};
