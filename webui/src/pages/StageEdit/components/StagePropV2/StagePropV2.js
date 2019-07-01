import _ from 'lodash';
import * as PIXI from "pixi.js";
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { svgPathProperties } from "svg-path-properties";
import { selectStageProp, updateStageProp } from '../../actions';
import stagePropTypes from '../../stagePropTypes';

const lineWidth = 2;
const lineColor = 0xffffff;
const padding = 5;
const names = { background: "Background", path: "Path" };

class StagePropV2 extends Component {

  state = {
    pixiContainer: null,
    dragState: null
  };

  componentDidMount() {
    this.buildStageProp();
  }

  componentDidUpdate(prevProps) {
    if (this.state.pixiContainer && this.props.stageProp !== prevProps.stageProp) {
      this.destroyContainer();
      this.buildStageProp();
    }
  }

  render() {
    return <></>;
  }

  componentWillUnmount() {
    this.destroyContainer();
  }

  buildStageProp() {
    const { pixiApp, stageProp } = this.props;

    const pathProperties = svgPathProperties(stagePropTypes[stageProp.type].path);
    const { points, width, height } = this.getLinePoints(pathProperties, stageProp);

    const pixiContainer = this.buildContainer(stageProp, width, height);
    pixiContainer.addChild(this.buildBackground(stageProp, width, height));
    pixiContainer.addChild(this.buildPath(points));
    pixiApp.stage.addChild(pixiContainer);

    this.setState({ pixiContainer });
  }

  buildContainer(stageProp, width, height) {
    const pixiContainer = new PIXI.Container();
    pixiContainer.position = { x: stageProp.positionX + (width / 2), y: stageProp.positionY + (height / 2) };
    pixiContainer.rotation = stageProp.rotation / 180 * Math.PI;
    pixiContainer.scale = { x: 1, y: 1 };
    pixiContainer.pivot = { x: width / 2, y: height / 2 };
    return pixiContainer;
  }

  buildBackground(stageProp, width, height) {
    const background = new PIXI.Graphics();
    background.name = names.background;

    background.beginFill(0xFF00FF);
    background.drawRect(-padding, -padding, width + (2 * padding), height + (2 * padding));
    background.endFill();

    background.buttonMode = true;
    background.interactive = true;
    background.on('pointerup', () => this.props.selectStageProp(stageProp.uuid));

    background
      .on('pointerdown', this.onDragStart)
      .on('pointermove', this.onDragMove)
      .on('pointerup', this.onDragEnd)
      .on('pointerupoutside', this.onDragEnd);

    return background;
  }

  onDragStart = event => {
    const { pixiContainer } = this.state;
    this.setState({
      dragState: {
        x: pixiContainer.x,
        y: pixiContainer.y,
        mouseX: event.data.originalEvent.clientX,
        mouseY: event.data.originalEvent.clientY
      }
    });
  }

  onDragMove = event => {
    const { dragState, pixiContainer } = this.state;

    if (dragState) {
      pixiContainer.x = dragState.x + (event.data.originalEvent.clientX - dragState.mouseX);
      pixiContainer.y = dragState.y + (event.data.originalEvent.clientY - dragState.mouseY);
    }
  }

  onDragEnd = event => {
    const { stageProp } = this.props;
    const { dragState } = this.state;

    this.setState({ dragState: null });
    this.props.updateStageProp({
      ...stageProp,
      positionX: stageProp.positionX + (event.data.originalEvent.clientX - dragState.mouseX),
      positionY: stageProp.positionY + (event.data.originalEvent.clientY - dragState.mouseY)
    });
  }

  buildPath(points) {
    const path = new PIXI.Graphics();
    path.name = names.path;
    path.lineStyle(lineWidth, lineColor);

    const origin = points[0];
    path.moveTo(origin.x, origin.y);
    _.forEach(points, point => path.lineTo(point.x, point.y));
    return path;
  }

  getLinePoints(pathProperties, stageProp) {
    const length = pathProperties.getTotalLength();

    const box = { x1: Number.MAX_VALUE, y1: Number.MAX_VALUE, x2: Number.MIN_VALUE, y2: Number.MIN_VALUE };
    const linePoints = [];

    const pointCount = Math.floor(length);
    _.forEach(Array(pointCount + 1), (_, i) => {
      const progress = length * (i / pointCount);
      const point = pathProperties.getPointAtLength(progress);
      linePoints.push(point);

      box.x1 = Math.min(box.x1, point.x);
      box.y1 = Math.min(box.y1, point.y);
      box.x2 = Math.max(box.x2, point.x);
      box.y2 = Math.max(box.y2, point.y);
    });

    const width = (box.x2 - box.x1) * stageProp.scaleX;
    const height = (box.y2 - box.y1) * stageProp.scaleY;

    const points = _.map(linePoints, point => ({
      x: (point.x - box.x1) * stageProp.scaleX,
      y: (point.y - box.y1) * stageProp.scaleY
    }));

    return { points, width, height };
  }

  destroyContainer() {
    this.state.pixiContainer.destroy({ children: true });
  }
}

function mapStateToProps({ page: { sequenceEdit, stageEdit } }) {
  return {
    stage: stageEdit.present.stage
  };
}

export default connect(mapStateToProps, { updateStageProp, selectStageProp })(StagePropV2);
