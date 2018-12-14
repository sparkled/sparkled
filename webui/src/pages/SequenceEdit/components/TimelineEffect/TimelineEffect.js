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
    this.moveEffect = this.moveEffect.bind(this);
    this.selectEffect = this.selectEffect.bind(this);
    this.rndRef = React.createRef();
  }

  shouldComponentUpdate(nextProps) {
    const { effect, selectedEffect } = this.props;
    const effectChanged = effect !== nextProps.effect;
    const selectedEffectChanged = (effect === selectedEffect) !== (nextProps.effect === nextProps.selectedEffect);

    return effectChanged || selectedEffectChanged;
  }

  componentWillReceiveProps(nextProps) {
    if (this.rndRef.current) {
      const dimensions = this.getDimensions(nextProps);
      this.rndRef.current.updateSize(dimensions);
      this.rndRef.current.updatePosition(dimensions);
    }
  }

  render() {
    const dimensions = this.getDimensions(this.props);

    return (
      <Rnd className={'effect ' + this.getEffectClass()} ref={this.rndRef}
           bounds="parent"
           default={dimensions}
           enableResizing={{ left: true, right: true }}
           dragAxis="x"
           dragGrid={[2, 0]}
           resizeGrid={[2, 0]}
           onMouseDown={this.onEffectClick}
           onDragStart={this.selectEffect}
           onDragStop={this.onEffectMove}
           onResizeStart={this.selectEffect}
           onResizeStop={this.onEffectResize}/>
    );
  }

  getEffectClass() {
    const { effect, selectedEffect } = this.props;
    if (selectedEffect && effect.uuid === selectedEffect.uuid) {
      return 'effect-active';
    } else if (effect.invalid) {
      return 'effect-invalid';
    } else {
      return '';
    }
  }

  getDimensions(props) {
    const { effect } = props;
    const x = effect.startFrame * 2;
    const width = (effect.endFrame - effect.startFrame + 1) * 2;
    return { width, height: 'inherit', x, y: 0 };
  }

  onEffectMove(event, data) {
    const { effect } = this.props;
    const startFrame = Math.floor(data.x / 2);
    const endFrame = Math.round(startFrame + (effect.endFrame - effect.startFrame));

    this.moveEffect(startFrame, endFrame);
  }

  onEffectResize(event, data, ref, delta, position) {
    const { effect } = this.props;
    const isLeft = (data === 'left');

    let startFrame = effect.startFrame;
    let endFrame = effect.endFrame;

    if (isLeft) {
      startFrame = Math.floor(effect.startFrame - (delta.width / 2));
    } else {
      endFrame = Math.round(effect.endFrame + (delta.width / 2));
    }

    this.moveEffect(startFrame, endFrame);
  }

  moveEffect(startFrame, endFrame) {
    const { channel, effect, sequence } = this.props;

    startFrame = Math.max(startFrame, 0);
    endFrame = Math.min(endFrame, sequence.frameCount - 1);

    if (effect.startFrame !== startFrame || effect.endFrame !== endFrame) {
      this.props.updateEffect(channel, { ...effect, startFrame, endFrame });
    }
  }

  onEffectClick(event) {
    this.selectEffect();
    event.stopPropagation(); // Prevent channel click handler from firing and deselecting the effect.
  }

  selectEffect() {
    const { channel, effect } = this.props;
    this.props.selectEffect(channel, effect);
  }
}

function mapStateToProps({ page }) {
  const { sequence, selectedEffect } = page.sequenceEdit.present;
  return { sequence, selectedEffect };
}

export default connect(mapStateToProps, { selectEffect, updateEffect })(TimelineEffect);
