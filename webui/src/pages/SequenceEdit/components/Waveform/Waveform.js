import React, { Component } from 'react';
import { connect } from 'react-redux';
import WaveSurfer from 'wavesurfer.js';
import './Waveform.css';
import { selectFrame } from '../../actions';
import * as restConfig from '../../../../config/restConfig';

class Waveform extends Component {

  state = {
    waveformId: `waveform-${+new Date()}`,
  }

  constructor(props) {
    super(props);
    this.selectFrame = this.selectFrame.bind(this);
  }

  render() {
    const { sequence, pixelsPerFrame } = this.props;
    const width = sequence.durationFrames * pixelsPerFrame;

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
      waveSurfer.init();

      const url = `${restConfig.ROOT_URL}/sequences/${sequence.id}/audio`;
      waveSurfer.load(url);
      waveSurfer.on('audioprocess', progress => this.selectFrame(progress / waveSurfer.getDuration()));
      waveSurfer.on('seek', this.selectFrame);
    }
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

  selectFrame(progressNormalised) {
    const { sequence, selectFrame } = this.props;
    const frameNumber = Math.floor(sequence.durationFrames * progressNormalised);
    selectFrame(frameNumber);
  }
}

function mapStateToProps({ page }) {
  const { sequence, pixelsPerFrame } = page.sequenceEdit.present;
  return { sequence, pixelsPerFrame };
}

export default connect(mapStateToProps, { selectFrame })(Waveform);
