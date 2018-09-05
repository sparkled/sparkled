import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Rnd } from 'react-rnd';
import { selectEffect, updateEffect } from '../../actions';
import './TimelineEffect.css';

class TimelineEffect extends Component {

  constructor(props) {
    super(props);
    this.onEffectMove = this.onEffectMove.bind(this);
    this.onEffectResize = this.onEffectResize.bind(this);
    this.onEffectClick = this.onEffectClick.bind(this);
    this.rndRef = React.createRef();
  }

  shouldComponentUpdate(newProps) {
    const { effect, selectedEffect } = this.props;
    const effectChanged = effect !== newProps.effect;
    const selectedEffectChanged = (effect === selectedEffect) !== (newProps.effect === newProps.selectedEffect);

    return effectChanged || selectedEffectChanged;
  }

  componentWillReceiveProps(newProps) {
    if (this.rndRef.current) {
      const dimensions = this.getDimensions(newProps);
      this.rndRef.current.updateSize(dimensions);
      this.rndRef.current.updatePosition(dimensions);
    }
  }

  render() {
    const { effect, channel, selectedEffect } = this.props;
    const dimensions = this.getDimensions(this.props);
    const activeClass = (selectedEffect && effect.uuid === selectedEffect.uuid) ? 'effect-active' : '';

    return (
      <Rnd className={'effect ' + activeClass} ref={this.rndRef}
           bounds="parent"
           default={dimensions}
           enableResizing={{ left: true, right: true }}
           dragAxis="x"
           dragGrid={[1, 0]}
           resizeGrid={[2, 0]}
           onDragStop={this.onEffectMove}
           onResizeStop={this.onEffectResize}
           onMouseDown={event => this.onEffectClick(event, channel, effect)}/>
    );
  }

  getDimensions(props) {
    const { effect } = props;
    const x = effect.startFrame * 2;
    const width = (effect.endFrame - effect.startFrame + 1) * 2;
    return { width, height: 'inherit', x, y: 0};
  }

  onEffectMove(event, data) {
    const { channel, effect } = this.props;
    const startFrame = Math.floor(data.x / 2);
    const endFrame = Math.round(startFrame + (effect.endFrame - effect.startFrame));

    if (effect.startFrame !== startFrame || effect.endFrame !== endFrame) {
      this.props.updateEffect(channel, { ...effect, startFrame, endFrame });
    }
  }

  onEffectResize(event, data, ref, delta, position) {
    const { channel, effect } = this.props;
    const isLeft = (data === 'left');

    const startFrame = isLeft ? effect.startFrame - (delta.width / 2) : effect.startFrame;
    const endFrame = isLeft ? effect.endFrame : effect.endFrame + (delta.width / 2);

    if (effect.startFrame !== startFrame || effect.endFrame !== endFrame) {
      this.props.updateEffect(channel, { ...effect, startFrame, endFrame });
    }
  }

  onEffectClick(event, channel, effect) {
    this.props.selectEffect(channel, effect);
    event.stopPropagation(); // Prevent channel click handler from firing and deselecting the effect.
  }
}

function mapStateToProps({ page }) {
  const { selectedEffect } = page.sequenceEdit.present;
  return { selectedEffect };
}

export default connect(mapStateToProps, { selectEffect, updateEffect })(TimelineEffect);
