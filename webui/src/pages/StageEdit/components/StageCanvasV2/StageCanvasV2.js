import _ from 'lodash';
import * as PIXI from 'pixi.js';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { clamp } from '../../../../utils/numberUtils';
import { selectStageProp } from '../../actions';
import StagePropV2 from '../StagePropV2';

class StageCanvasV2 extends Component {

  state = {
    canvasId: `stage-canvas-${+new Date()}`,
    pixiApp: null
  };

  componentDidMount() {
    const canvasContainer = document.querySelector(`#${this.state.canvasId}`);
    const parent = canvasContainer.parentElement;

    const pixiApp = new PIXI.Application({ antialias: true, resolution: window.devicePixelRatio || 1 });
    pixiApp.resizeTo = parent;
    pixiApp.view.style.width = pixiApp.view.style.height = '100%';
    canvasContainer.appendChild(pixiApp.view);

    this.setState({ pixiApp }, () => {
      pixiApp.renderer.plugins.interaction
        .on('pointerdown', this.onDragStart)
        .on('pointermove', this.onDragMove)
        .on('pointerup', this.onDragEnd)
        .on('pointerupoutside', this.onDragEnd);

      pixiApp.view.addEventListener('wheel', this.onZoom);
    });
  }

  onZoom = event => {
    const { pixiApp } = this.state;
    const newScale = clamp(pixiApp.stage.scale.x + (event.deltaY / -30), .25, 5);
    pixiApp.stage.scale.x = pixiApp.stage.scale.y = newScale;
  }

  onDragStart = event => {
    if (event.target !== null) {
      // Only drag if the background is selected.
      return;
    }

    const { pixiApp } = this.state;
    const { clientX, clientY } = event.data.originalEvent;
    this.setState({
      dragState: { originX: pixiApp.stage.x, originY: pixiApp.stage.y, mouseX: clientX, mouseY: clientY }
    });
  }

  onDragMove = event => {
    const { dragState, pixiApp } = this.state;

    if (dragState) {
      const { originX, originY, mouseX, mouseY } = dragState;
      const { clientX, clientY } = event.data.originalEvent;

      pixiApp.stage.x = originX + (clientX - mouseX);
      pixiApp.stage.y = originY + (clientY - mouseY);
    }
  }

  onDragEnd = event => {
    this.setState({ dragState: null });
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
