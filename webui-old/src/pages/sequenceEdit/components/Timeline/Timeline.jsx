import _ from 'lodash'
import React, { useMemo } from 'react'
import CurrentFrameIndicator from '../CurrentFrameIndicator'
import PlaybackFrameIndicator from '../PlaybackFrameIndicator'
import TimeIndicator from '../TimeIndicator'
import TimelineChannel from '../TimelineChannel'
import Waveform from '../Waveform'
import './Timeline.css'

const Timeline = ({ sequence, pixelsPerFrame }) => {
  const channels = useMemo(() => {
    return _.map(sequence.channels, channel => <TimelineChannel key={channel.id} channel={channel} />)
  }, [sequence.channels])

  return (
    <div className='timeline' onScroll={handleScroll}>
      <div className='timeline-container'>
        <div className='channels'>
          <div className='channel-wrapper'>
            <CurrentFrameIndicator />
            <PlaybackFrameIndicator />
            {channels}
            <Waveform />
            <TimeIndicator sequence={sequence} pixelsPerFrame={pixelsPerFrame} />
          </div>
        </div>
      </div>
    </div>
  )
}

function handleScroll(event) {
  // TODO: Remove this hack.
  var labels = event.target.querySelectorAll('.label')
  _.forEach(labels, label => {
    label.style.left = event.target.scrollLeft + 'px'
  })
}

export default Timeline
