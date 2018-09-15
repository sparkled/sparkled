import React, { Component } from 'react';
import { connect } from 'react-redux';
import './CurrentFrameIndicator.css';

class CurrentFrameIndicator extends Component {

  render() {
    const { currentFrame, pixelsPerFrame } = this.props;
    const style = {
      width: pixelsPerFrame,
      left: currentFrame * pixelsPerFrame
    };

    return (
      <div className="current-frame-indicator" style={style}/>
    );
  }
}

function mapStateToProps({ page }) {
  const { present } = page.sequenceEdit;
  const { currentFrame, pixelsPerFrame } = present;
  return { currentFrame, pixelsPerFrame };
}

export default connect(mapStateToProps, {})(CurrentFrameIndicator);
