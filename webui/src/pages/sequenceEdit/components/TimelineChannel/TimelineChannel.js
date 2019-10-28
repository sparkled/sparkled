import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import TimelineEffect from '../TimelineEffect';
import { selectEffect, selectFrame } from '../../actions';
import './TimelineChannel.css';

class TimelineChannel extends Component {

  render() {
    const { channel, pixelsPerFrame, sequence, selectedChannel } = this.props;
    const width = sequence.frameCount * pixelsPerFrame;

    const isActiveChannel = selectedChannel && channel.uuid === selectedChannel.uuid;
    const activeClass = isActiveChannel ? 'channel-active' : '';

    return (
      <div className={'channel ' + activeClass} style={{ width }} onMouseDown={this.onChannelClick}>
        <div className="label" onMouseDown={this.onLabelClick}>{channel.name}</div>
        {this.renderEffects()}
      </div>
    );
  }

  renderEffects() {
    const { channel } = this.props;
    return _.map(
      channel.effects,
      effect => <TimelineEffect key={effect.uuid} channel={channel} effect={effect}/>
    );
  }

  onChannelClick = event => {
    const { channel, pixelsPerFrame, selectEffect, selectFrame } = this.props;
    selectEffect(channel, null);

    // TODO: Remove this hack.
    const scrollLeft = document.querySelector('.channels').scrollLeft;
    const frame = Math.round((event.clientX + scrollLeft - 100) / pixelsPerFrame);
    selectFrame(frame);
  }

  onLabelClick = event => {
    const { channel, selectEffect } = this.props;
    selectEffect(channel, null);
    event.stopPropagation();
  }
}

function mapStateToProps({ page }) {
  const { currentFrame, pixelsPerFrame, sequence, song, selectedChannel } = page.sequenceEdit.present;
  return { currentFrame, pixelsPerFrame, sequence, song, selectedChannel };
}

export default connect(mapStateToProps, { selectEffect, selectFrame })(TimelineChannel);
