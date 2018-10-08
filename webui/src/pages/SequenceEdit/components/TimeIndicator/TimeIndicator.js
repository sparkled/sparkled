import React from 'react';
import './TimeIndicator.css';


const TimeIndicator = ({ sequence, pixelsPerFrame }) => {
  const { durationFrames, framesPerSecond } = sequence;
  const width = durationFrames * pixelsPerFrame;

  const seconds = renderSeconds(durationFrames, framesPerSecond, pixelsPerFrame);
  return (
    <svg className="time-indicator" style={{ width }}>
      <pattern id="frame-pattern" width={pixelsPerFrame} height="10" patternUnits="userSpaceOnUse">
        <line x1="0" y1="0" x2="0" y2="3"/>
      </pattern>

      <pattern id="quarter-second-pattern" width={pixelsPerFrame * (framesPerSecond / 4)} height="10"
               patternUnits="userSpaceOnUse">
        <line x1="0" y1="3" x2="0" y2="6"/>
      </pattern>

      <pattern id="half-second-pattern" width={pixelsPerFrame * (framesPerSecond / 2)} height="10"
               patternUnits="userSpaceOnUse">
        <line x1="0" y1="3" x2="0" y2="8"/>
      </pattern>

      <pattern id="second-pattern" width={pixelsPerFrame * framesPerSecond} height="10" patternUnits="userSpaceOnUse">
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

function renderSeconds(durationFrames, framesPerSecond, pixelsPerFrame) {
  const seconds = [];
  for (let i = 0; i < Math.ceil(durationFrames / framesPerSecond); i++) {
    // 1970-01-01T00:01:23.000Z
    const hhss = new Date(i * 1000).toISOString().substr(14, 5);

    const left = i * framesPerSecond * pixelsPerFrame;
    seconds.push(<text key={i} x={left} y={20}>{hhss}</text>);
  }

  return seconds;
}

export default TimeIndicator;
