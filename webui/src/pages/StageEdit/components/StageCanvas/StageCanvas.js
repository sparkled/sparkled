import _ from 'lodash';
import * as PIXI from 'pixi.js';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { clamp } from '../../../../utils/numberUtils';
import { selectStageProp } from '../../actions';
import StageProp from '../StageProp';

const gridSize = 20;
const zoomLimits = { min: .5, max: 5 };

class StageCanvas extends Component {

  state = {
    canvasId: `stage-canvas-${+new Date()}`,
    pixiApp: null
  };

  componentDidMount() {
    const canvasContainer = document.querySelector(`#${this.state.canvasId}`);
    const parent = canvasContainer.parentElement;

    const resolution = window.devicePixelRatio || 1;
    const pixiApp = new PIXI.Application({ resolution, antialias: true, transparent: true });
    pixiApp.resizeTo = parent;
    pixiApp.view.style.width = pixiApp.view.style.height = '100%';
    pixiApp.stage.addChild(this.buildGrid());
    canvasContainer.appendChild(pixiApp.view);

    this.setState({ pixiApp }, () => {
      this.redrawGrid();

      pixiApp.renderer.plugins.interaction
        .on('pointerdown', this.onDragStart)
        .on('pointermove', this.onDragMove)
        .on('pointerup', this.onDragEnd)
        .on('pointerupoutside', this.onDragEnd);

      pixiApp.view.addEventListener('wheel', this.onZoom);
    });
  }

  buildGrid = () => {
    const grid = new PIXI.Graphics();
    grid.position.x = grid.position.y = 0;
    grid.name = "Grid";
    return grid;
  }

  onZoom = event => {
    const { pixiApp } = this.state;

    const newScale = clamp(pixiApp.stage.scale.x + (event.deltaY / -30), zoomLimits.min, zoomLimits.max);
    pixiApp.stage.scale.x = pixiApp.stage.scale.y = newScale;

    this.redrawGrid();
  }

  onDragStart = event => {
    if (event.target !== null) {
      // Only drag if the background is selected.
      return;
    }

    const { pixiApp } = this.state;
    const { clientX, clientY, changedTouches } = event.data.originalEvent;
    let mouseX = clientX !== undefined ? clientX : changedTouches[0].clientX;
    let mouseY = clientY !== undefined ? clientY : changedTouches[0].clientY;

    this.setState({
      dragState: { originX: pixiApp.stage.x, originY: pixiApp.stage.y, mouseX, mouseY }
    });
  }

  onDragMove = event => {
    const { dragState, pixiApp } = this.state;

    if (dragState) {
      const { originX, originY, mouseX, mouseY } = dragState;

      const { clientX, clientY, changedTouches } = event.data.originalEvent;
      let newMouseX = clientX !== undefined ? clientX : changedTouches[0].clientX;
      let newMouseY = clientY !== undefined ? clientY : changedTouches[0].clientY;

      pixiApp.stage.x = originX + (newMouseX - mouseX);
      pixiApp.stage.y = originY + (newMouseY - mouseY);
      this.redrawGrid();
    }
  }

  onDragEnd = event => {
    this.setState({ dragState: null });
  }

  redrawGrid = () => {
    const { pixiApp } = this.state;
    const grid = pixiApp.stage.getChildByName("Grid");
    grid.clear();

    const scale = pixiApp.stage.scale.x;

    // Calculate left and top positions for the grid, and subtract gridSize to allow for partial grid cells to be drawn.
    let startX = (-pixiApp.stage.x / scale) - gridSize;
    let startY = (-pixiApp.stage.y / scale) - gridSize;

    // Adjust positions to account for the grid offset, otherwise the grid will always be drawn from the top-left.
    startX -= startX % gridSize;
    startY -= startY % gridSize;

    // Calculate the width and height, and add gridSize to allow for partial grid cells to be drawn.
    const width = pixiApp.view.clientWidth / scale + gridSize * 2;
    const height = pixiApp.view.clientHeight / scale + gridSize * 2;

    grid.lineStyle(1, 0xffffff, .2, 1);

    for (let x = 0; x < width; x += gridSize) {
      grid.moveTo(x + startX, startY);
      grid.lineTo(x + startX, height + startY);
    }

    for (let y = 0; y < height; y += gridSize) {
      grid.moveTo(startX, y + startY);
      grid.lineTo(width + startX, y + startY);
    }
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
      return <StageProp key={key} stageProp={stageProp} pixiApp={pixiApp} editable={editable}/>;
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

export default connect(mapStateToProps, { selectStageProp })(StageCanvas);
