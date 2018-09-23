import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import CurrentFrameIndicator from '../CurrentFrameIndicator';
import TimelineChannel from '../TimelineChannel';
import TimeIndicator from '../TimeIndicator';
import './Timeline.css';

class Timeline extends Component {

  render() {
    const { sequence } = this.props;

    return (
      <div className="timeline" onScroll={this.handleScroll}>
        <div className="timeline-container">
          <div className="channels">
            <div className="channel-wrapper">
              <CurrentFrameIndicator/>
              {_.map(sequence.channels, channel => <TimelineChannel key={channel.uuid} channel={channel}/>)}
              <TimeIndicator/>
            </div>
          </div>
        </div>
      </div>
    );
  }

  handleScroll(event) {
    var labels = event.target.querySelectorAll('.label');
    _.forEach(labels, label => {
      label.style.left = event.target.scrollLeft + 'px';
    });
  }
}

function mapStateToProps({ page }) {
  const { sequence, stage, stageProps } = page.sequenceEdit.present;
  return { sequence, stage, stageProps };
}

export default connect(mapStateToProps, { })(Timeline);
