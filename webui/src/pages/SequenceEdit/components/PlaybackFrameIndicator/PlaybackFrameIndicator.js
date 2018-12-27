import React, { Component } from 'react';
import { connect } from 'react-redux';
import { eventType, subscribe, unsubscribe } from '../../../../utils/eventBus';
import './PlaybackFrameIndicator.css';

const key = 'PlaybackFrameIndicator';

class PlaybackFrameIndicator extends Component {

  constructor(props) {
    super(props);
    this.ref = React.createRef();
  }

  componentDidMount() {
    subscribe(eventType.RENDER_DATA, key, this.updateIndicator);
  }

  componentWillReceiveProps(nextProps, nextContext) {
    if (!nextProps.renderData) {
      this.updateIndicator({ renderData: null });
    }
  }

  render() {
    const style = {
      width: this.props.pixelsPerFrame,
      display: 'none'
    };

    return (
      <div className="PlaybackFrameIndicator" style={style} ref={this.ref}/>
    );
  }

  componentWillUnmount() {
    unsubscribe(eventType.RENDER_DATA, this);
  }

  updateIndicator = ({ renderData, playbackFrame }) => {
    const node = this.ref.current;

    if (renderData) {
      node.style.display = '';
      node.style.left = `${(playbackFrame) * this.props.pixelsPerFrame}px`;
    } else {
      node.style.display = 'none';
    }
  }
}

function mapStateToProps({ page }) {
  const { present } = page.sequenceEdit;
  const { currentFrame, pixelsPerFrame, renderData } = present;
  return { currentFrame, pixelsPerFrame, renderData };
}

export default connect(mapStateToProps, {})(PlaybackFrameIndicator);
