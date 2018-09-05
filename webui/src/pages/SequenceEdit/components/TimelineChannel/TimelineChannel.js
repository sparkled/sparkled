import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import TimelineEffect from '../TimelineEffect';
import { selectEffect } from '../../actions';
import './TimelineChannel.css';

class TimelineChannel extends Component {

  render() {
    const { channel, sequence, selectedChannel, selectEffect } = this.props;
    const width = sequence.durationFrames * 2;
    const activeClass = (selectedChannel && channel.uuid === selectedChannel.uuid) ? 'channel-active' : '';
    const effects = _.map(channel.effects, effect => <TimelineEffect key={effect.uuid} channel={channel} effect={effect}/>);

    return (
      <div className={'channel ' + activeClass} style={{ width }} onMouseDown={event => selectEffect(channel.uuid, null)}>
        <div className="label">Label</div>
        {effects}
      </div>
    );
  }
}

function mapStateToProps({ page }) {
  const { sequence, selectedChannel } = page.sequenceEdit.present;
  return { sequence, selectedChannel };
}

export default connect(mapStateToProps, { selectEffect })(TimelineChannel);
