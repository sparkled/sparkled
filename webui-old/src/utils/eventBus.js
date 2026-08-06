import _ from 'lodash'

const subscribers = {}

export const eventType = {
  RENDER_DATA: 'RENDER_DATA'
}

export const subscribe = (eventType, callback) => {
  if (!subscribers[eventType]) {
    subscribers[eventType] = []
  }

  subscribers[eventType].push(callback)
}

export const unsubscribe = (eventType, callback) => {
  if (subscribers[eventType]) {
    _.remove(subscribers[eventType], c => c === callback)
  }
}

export const publish = (eventType, data) => {
  if (subscribers[eventType]) {
    _(subscribers[eventType]).forEach(callback => callback(data))
  }
}
