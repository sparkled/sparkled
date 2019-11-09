import React from 'react'

export default ({ millis }) => {
  return <span>{formatTime(millis)}</span>
}

function formatTime(millis) {
  if (millis == null) {
    return ''
  }

  const hours = Math.floor(millis / 3600)
  const minutes = Math.floor((millis % 3600) / 60)
  const seconds = Math.floor(millis % 60)

  if (hours > 0) {
    return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`
  } else {
    return `${pad(minutes)}:${pad(seconds)}`
  }
}

function pad(millis) {
  return millis < 10 ? '0' + millis : millis
}
