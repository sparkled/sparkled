import React from 'react'
import './TimeIndicator.css'
import { getFormattedDuration } from '../../../../utils/dateUtils'

const TimeIndicator = ({ sequence, pixelsPerFrame }) => {
  const { frameCount, framesPerSecond } = sequence
  const width = frameCount * pixelsPerFrame

  const seconds = renderSeconds(frameCount, framesPerSecond, pixelsPerFrame)
  return (
    <svg className="time-indicator" style={{ width }}>
      <pattern
        id="frame-pattern"
        width={pixelsPerFrame}
        height="10"
        patternUnits="userSpaceOnUse"
      >
        <rect width="1" height="3" />
      </pattern>

      <pattern
        id="quarter-second-pattern"
        width={pixelsPerFrame * (framesPerSecond / 4)}
        height="10"
        patternUnits="userSpaceOnUse"
      >
        <rect width="1" height="6" />
      </pattern>

      <pattern
        id="half-second-pattern"
        width={pixelsPerFrame * (framesPerSecond / 2)}
        height="10"
        patternUnits="userSpaceOnUse"
      >
        <rect width="1" height="8" />
      </pattern>

      <pattern
        id="second-pattern"
        width={pixelsPerFrame * framesPerSecond}
        height="10"
        patternUnits="userSpaceOnUse"
      >
        <rect width="1" height="10" />
      </pattern>

      <rect fill="url(#frame-pattern)" height="10" width={width} x="0" y="0" />
      <rect
        fill="url(#quarter-second-pattern)"
        height="10"
        width={width}
        x="0"
        y="0"
      />
      <rect
        fill="url(#half-second-pattern)"
        height="10"
        width={width}
        x="0"
        y="0"
      />
      <rect fill="url(#second-pattern)" height="10" width={width} x="0" y="0" />

      {seconds}
    </svg>
  )
}

function renderSeconds(frameCount, framesPerSecond, pixelsPerFrame) {
  const pixelsPerSecond = framesPerSecond * pixelsPerFrame

  const seconds = []
  for (let i = 0; i < Math.ceil(frameCount / framesPerSecond); i++) {
    const left = i * pixelsPerSecond
    const secondText = getFormattedDuration(i)

    if (framesPerSecond >= 30 || i % 2 === 0) {
      seconds.push(
        <text key={i} x={left} y={20}>
          {secondText}
        </text>
      )
    }
  }

  return seconds
}

export default TimeIndicator
