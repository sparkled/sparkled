import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import TimelineEffect from '../TimelineEffect';
import { selectEffect, selectFrame } from '../../actions';
import './TimelineChannel.css';

class TimelineChannel extends Component {

  constructor(props) {
    super(props);
    this.onChannelClick = this.onChannelClick.bind(this);
  }

  render() {
    const { channel, sequence, selectedChannel } = this.props;
    const width = sequence.durationFrames * 2;
    const activeClass = (selectedChannel && channel.uuid === selectedChannel.uuid) ? 'channel-active' : '';
    const effects = _.map(channel.effects, effect => <TimelineEffect key={effect.uuid} channel={channel} effect={effect}/>);

    return (
      <div className={'channel ' + activeClass} style={{ width }} onMouseDown={this.onChannelClick}>
        <div className="label">{channel.name}</div>
        {effects}
      </div>
    );
  }

  onChannelClick(event) {
    const { channel, pixelsPerFrame, selectEffect, selectFrame } = this.props;
    selectEffect(channel, null);

    const scrollLeft = document.querySelector('.channels').scrollLeft;
    const frame = Math.round((event.clientX + scrollLeft  - 100) / pixelsPerFrame);
    selectFrame(frame);
  }
}

function mapStateToProps({ page }) {
  const { currentFrame, pixelsPerFrame, sequence, selectedChannel } = page.sequenceEdit.present;
  return { currentFrame, pixelsPerFrame, sequence, selectedChannel };
}

export default connect(mapStateToProps, { selectEffect, selectFrame })(TimelineChannel);
