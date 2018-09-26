import React from 'react';
import './TimeIndicator.css';

const PIXELS_PER_FRAME = 2;

const TimeIndicator = ({ sequence }) => {
  const { durationFrames, framesPerSecond } = sequence;
  const width = durationFrames * PIXELS_PER_FRAME;

  const seconds = renderSeconds(durationFrames, framesPerSecond);
  return (
    <svg className="time-indicator" style={{ width }}>
      <pattern id="frame-pattern" width={PIXELS_PER_FRAME} height="10" patternUnits="userSpaceOnUse">
        <line x1="0" y1="0" x2="0" y2="3"/>
      </pattern>

      <pattern id="quarter-second-pattern" width={PIXELS_PER_FRAME * (framesPerSecond / 4)} height="10"
               patternUnits="userSpaceOnUse">
        <line x1="0" y1="3" x2="0" y2="6"/>
      </pattern>

      <pattern id="half-second-pattern" width={PIXELS_PER_FRAME * (framesPerSecond / 2)} height="10"
               patternUnits="userSpaceOnUse">
        <line x1="0" y1="3" x2="0" y2="8"/>
      </pattern>

      <pattern id="second-pattern" width={PIXELS_PER_FRAME * framesPerSecond} height="10" patternUnits="userSpaceOnUse">
        <line x1="0" y1="3" x2="0" y2="10"/>
      </pattern>

      <rect fill="url(#frame-pattern)" height="10" width={width} x="0" y="0"/>
      <rect fill="url(#quarter-second-pattern)" height="10" width={width} x="0" y="0"/>
      <rect fill="url(#half-second-pattern)" height="10" width={width} x="0" y="0"/>
      <rect fill="url(#second-pattern)" height="10" width={width} x="0" y="0"/>

      {seconds}
    </svg>
  );
};

function renderSeconds(durationFrames, framesPerSecond) {
  const seconds = [];
  for (let i = 0; i < Math.ceil(durationFrames / framesPerSecond); i++) {
    // 1970-01-01T00:01:23.000Z
    const hhss = new Date(i * 1000).toISOString().substr(14, 5);

    const left = i * framesPerSecond * PIXELS_PER_FRAME;
    seconds.push(<text key={i} x={left} y={20}>{hhss}</text>);
  }

  return seconds;
}

export default TimeIndicator;
