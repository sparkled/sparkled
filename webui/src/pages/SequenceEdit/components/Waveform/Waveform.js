import React, { Component } from 'react';
import { connect } from 'react-redux';
import WaveSurfer from 'wavesurfer.js';
import './Waveform.css';
import { selectFrame } from '../../actions';
import * as restConfig from '../../../../config/restConfig';
import { eventType, publish } from '../../../../utils/eventBus';

class Waveform extends Component {

  state = {
    waveformId: `waveform-${+new Date()}`,
  }

  waveSurfer = null

  componentWillReceiveProps(nextProps) {
    const { props, waveSurfer } = this;

    if (waveSurfer && props.renderData !== nextProps.renderData) {
      this.performWithoutSeek(() => {
        if (nextProps.renderData) {
          const { currentFrame, sequence } = nextProps;

          waveSurfer.seekTo(currentFrame / sequence.frameCount);
          waveSurfer.play();
        } else {
          waveSurfer.stop();
        }
      });
    }
  }

  render() {
    const { sequence, pixelsPerFrame } = this.props;
    const width = sequence.frameCount * pixelsPerFrame;

    return (
      <div className="WaveformContainer">
        <div id={this.state.waveformId} className="Waveform" style={{width}}/>
      </div>
    );
  }

  componentDidMount() {
    const { sequence } = this.props;
    if (sequence) {
      const waveSurfer = new WaveSurfer(this.getWaveSurferProperties(sequence));
      this.waveSurfer = waveSurfer;

      waveSurfer.init();

      const url = `${restConfig.ROOT_URL}/sequences/${sequence.id}/songAudio`;
      waveSurfer.load(url);
      waveSurfer.on('audioprocess', this.publishRenderedFrame);
      waveSurfer.on('seek', this.selectFrame);
    }
  }

  componentWillUnmount() {
    this.waveSurfer.destroy();
  }

  performWithoutSeek = callback => {
    this.waveSurfer.un('seek');
    callback();
    this.waveSurfer.on('seek', this.selectFrame);
  }

  getWaveSurferProperties(sequence) {
    return {
      container: '#' + this.state.waveformId,
      cursorWidth: 0,
      height: 50,
      waveColor: '#fff',
      progressColor: '#fff'
    };
  }

  publishRenderedFrame = progress => {
    const { currentFrame, renderData, sequence } = this.props;

    if (renderData) {
      const progressNormalised = progress / this.waveSurfer.getDuration();

      const playbackFrame = Math.floor(sequence.frameCount * progressNormalised);
      if (playbackFrame < currentFrame + renderData.frameCount - 1) {
        publish(eventType.RENDER_DATA, { renderData, playbackFrame });
      } else {
        publish(eventType.RENDER_DATA, {});
        this.performWithoutSeek(() => this.waveSurfer.stop());
      }
    }
  }

  selectFrame = progressNormalised => {
    const { sequence, selectFrame } = this.props;
    const frameNumber = Math.floor(sequence.frameCount * progressNormalised);
    selectFrame(frameNumber);
  }
}

function mapStateToProps({ page }) {
  const { currentFrame, sequence, pixelsPerFrame, renderData } = page.sequenceEdit.present;
  return { currentFrame, sequence, pixelsPerFrame, renderData };
}

export default connect(mapStateToProps, { selectFrame })(Waveform);
