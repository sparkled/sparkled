import _ from 'lodash';
import * as PIXI from 'pixi.js';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { selectStageProp } from '../../actions';
import StagePropV2 from '../StagePropV2';

class StageCanvasV2 extends Component {

  state = {
    canvasId: `stage-canvas-${+new Date()}`,
    pixiApp: null
  };

  componentDidMount() {
    const { width, height } = this.props.stage;
    const resolution = window.devicePixelRatio || 1;

    const pixiApp = new PIXI.Application({ antialias: true, width, height, resolution });
    pixiApp.view.style.width = width + "px";
    pixiApp.view.style.height = height + "px";
    document.querySelector(`#${this.state.canvasId}`).appendChild(pixiApp.view);

    this.setState({ pixiApp });
  }

  render() {
    const { className = '' } = this.props;

    return (
      <div id={this.state.canvasId} className={'stage-canvas ' + className}>
        {this.renderStageProps()}
      </div>
    );
  }

  renderStageProps() {
    const { pixiApp } = this.state;
    const { editable, stage } = this.props;

    if (!pixiApp) {
      return <></>;
    }

    return _.map(stage.stageProps, stageProp => {
      // Keying on the stage prop type will recreate the component with the correct path when its type changes.
      const key = stageProp.uuid + stageProp.type;
      return <StagePropV2 key={key} stageProp={stageProp} pixiApp={pixiApp} editable={editable}/>;
    });
  }

  componentWillUnmount() {
    const { pixiApp } = this.state;

    if (pixiApp) {
      pixiApp.destroy(true);
    }
  }
}

function mapStateToProps() {
  return {};
}

export default connect(mapStateToProps, { selectStageProp })(StageCanvasV2);
