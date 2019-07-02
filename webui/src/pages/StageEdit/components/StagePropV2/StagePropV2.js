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
const handleRadius = 5;
const quarterTurn = Math.PI / 2;
const fullCircle = Math.PI * 2;
const names = { background: "Background", path: "Path", rotateHandle: "RotateHandle" };

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

    const { path, segments } = stagePropTypes[stageProp.type];
    const pathProperties = svgPathProperties(path);
    const points = this.getLinePoints(pathProperties, stageProp, segments);

    const pixiPath = this.buildPath(points);
    const pathWidth = pixiPath.width - lineWidth;
    const pathHeight = pixiPath.height - lineWidth;
    const pixiContainer = this.buildContainer(stageProp, pathWidth, pathHeight);
    pixiContainer.addChild(this.buildBackground(stageProp, pathWidth, pathHeight));
    pixiContainer.addChild(pixiPath);
    pixiContainer.addChild(this.buildRotateHandle(stageProp, pathWidth, pathHeight));
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
    background
      .on('pointerdown', event => this.onDragStart(event, names.background))
      .on('pointermove', this.onDragMove)
      .on('pointerup', this.onDragEnd)
      .on('pointerupoutside', this.onDragEnd);

    return background;
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

  buildRotateHandle(stageProp, width, height) {
    const rotateHandle = new PIXI.Graphics();
    rotateHandle.name = names.rotateHandle;

    rotateHandle.beginFill(0x00FF00);
    rotateHandle.drawCircle(0, 0, handleRadius);
    rotateHandle.endFill();
    rotateHandle.position = { x: width / 2, y: handleRadius * -4 };

    rotateHandle.buttonMode = true;
    rotateHandle.interactive = true;
    rotateHandle
      .on('pointerdown', event => this.onDragStart(event, names.rotateHandle))
      .on('pointermove', this.onDragMove)
      .on('pointerup', this.onDragEnd)
      .on('pointerupoutside', this.onDragEnd);

    return rotateHandle;
  }

  onDragStart = (event, target) => {
    const { pixiContainer } = this.state;
    this.props.selectStageProp(this.props.stageProp.uuid);

    const { x, y } = event.data.getLocalPosition(pixiContainer.parent);
    this.setState({
      dragState: { target, originX: pixiContainer.x, originY: pixiContainer.y, mouseX: x, mouseY: y }
    });
  }

  onDragMove = event => {
    const { dragState, pixiContainer } = this.state;

    if (dragState) {
      const { originX, originY, mouseX, mouseY, target } = dragState;
      const { x: relativeParentX, y: relativeParentY } = event.data.getLocalPosition(pixiContainer.parent);

      if (target === names.background) {
        pixiContainer.x = originX + (relativeParentX - mouseX);
        pixiContainer.y = originY + (relativeParentY - mouseY);
      } else if (dragState.target === names.rotateHandle) {
        const rotateHandle = pixiContainer.getChildByName(names.rotateHandle);
        rotateHandle.position.y = event.data.getLocalPosition(pixiContainer).y;

        // Pixi's rotation starts at North, but atan2 starts at East, so need to rotate back one quarter.
        let newRotation = Math.atan2(originY - relativeParentY, originX - relativeParentX) - quarterTurn;
        newRotation %= fullCircle;
        newRotation += newRotation < 0 ? fullCircle : 0;
        pixiContainer.rotation = newRotation;
      }
    }
  }

  onDragEnd = event => {
    const { stageProp } = this.props;
    const { dragState, pixiContainer } = this.state;
    const { x, y } = event.data.getLocalPosition(pixiContainer.parent);

    if (dragState) {
      if (dragState.target === names.background) {
        const offsetX = x - dragState.mouseX;
        const offsetY = y - dragState.mouseY;
        this.props.updateStageProp({
          ...stageProp,
          positionX: Math.round(stageProp.positionX + offsetX),
          positionY: Math.round(stageProp.positionY + offsetY)
        });
      } else if (dragState.target === names.rotateHandle) {
        const rotation = Math.round(pixiContainer.rotation / Math.PI * 180);
        this.props.updateStageProp({ ...stageProp, rotation });
      }
    }

    this.setState({ dragState: null });
  }

  getLinePoints(pathProperties, stageProp, segments) {
    const length = pathProperties.getTotalLength();

    const linePoints = [];
    const pointCount = Math.floor(length);
    _.forEach(Array(pointCount + 1), (_, i) => {
      const progress = length * (i / pointCount);
      const point = pathProperties.getPointAtLength(progress);
      linePoints.push(point);
    });

    return _.map(linePoints, point => ({ x: point.x * stageProp.scaleX, y: point.y * stageProp.scaleY }));
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
