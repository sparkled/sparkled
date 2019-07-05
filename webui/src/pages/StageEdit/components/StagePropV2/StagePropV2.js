import _ from 'lodash';
import * as PIXI from "pixi.js";
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { svgPathProperties } from "svg-path-properties";
import { selectStageProp, updateStageProp } from '../../actions';
import stagePropTypes from '../../stagePropTypes';
import StagePropBackground from './StagePropBackground';

const lineWidth = 3;
const lineColor = 0xffffff;
const handleRadius = 6;
const quarterTurn = Math.PI / 2;
const fullCircle = Math.PI * 2;
const names = { background: "Background", path: "Path", rotateHandle: "RotateHandle" };

class StagePropV2 extends Component {

  state = {
    pixiContainer: null,
    dragState: null
  };

  componentDidMount() {
    console.log('Mounted');
    this.buildStageProp();
  }

  async componentDidUpdate(prevProps) {
    if (this.state.pixiContainer && this.props.stageProp !== prevProps.stageProp) {
      await this.destroyContainer();
      this.buildStageProp();
    }
  }

  render() {
    const { pixiContainer, width, height } = this.state;
    return !pixiContainer ? <></> : (
      <>
        <StagePropBackground pixiContainer={pixiContainer} width={width} height={height} onMoved={this.onMoved}/>
      </>
    );
  }

  async componentWillUnmount() {
    console.log('Destroying.');
    await this.destroyContainer();
  }

  onMoved = (offsetX, offsetY) => {
    const { stageProp } = this.props;
    this.props.updateStageProp({
      ...stageProp,
      positionX: Math.round(stageProp.positionX + offsetX),
      positionY: Math.round(stageProp.positionY + offsetY)
    });
  }

  buildStageProp() {
    const { pixiApp, stageProp } = this.props;

    const { path, segments } = stagePropTypes[stageProp.type];
    const pathProperties = svgPathProperties(path);
    const points = this.getLinePoints(pathProperties, stageProp, segments);

    const pixiPath = this.buildPath(points);
    const width = pixiPath.width - lineWidth;
    const height = pixiPath.height - lineWidth;
    const pixiContainer = this.buildContainer(stageProp, width, height);
    pixiContainer.addChild(pixiPath);
    pixiContainer.addChild(this.buildRotateHandle(stageProp, width, height));
    pixiApp.stage.addChild(pixiContainer);

    this.setState({ pixiContainer, width, height });
  }

  buildContainer(stageProp, width, height) {
    const pixiContainer = new PIXI.Container();
    pixiContainer.sortableChildren = true;
    pixiContainer.position = { x: stageProp.positionX + (width / 2), y: stageProp.positionY + (height / 2) };
    pixiContainer.rotation = stageProp.rotation / 180 * Math.PI;
    pixiContainer.scale = { x: 1, y: 1 };
    pixiContainer.pivot = { x: width / 2, y: height / 2 };
    return pixiContainer;
  }

  buildPath(points) {
    const path = new PIXI.Graphics();
    path.name = names.path;
    path.zIndex = 2;
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
    this.setState({ dragState: { target, originX: pixiContainer.x, originY: pixiContainer.y } });
  }

  onDragMove = event => {
    const { dragState, pixiContainer } = this.state;

    if (dragState) {
      const { originX, originY } = dragState;
      const { x: relativeParentX, y: relativeParentY } = event.data.getLocalPosition(pixiContainer.parent);

      if (dragState.target === names.rotateHandle) {
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

    if (dragState) {
      if (dragState.target === names.rotateHandle) {
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
    return new Promise(resolve => {
      this.state.pixiContainer.destroy({ children: true });
      this.setState({ pixiContainer: null }, resolve);
    });
  }
}

function mapStateToProps({ page: { sequenceEdit, stageEdit } }) {
  return {
    stage: stageEdit.present.stage
  };
}

export default connect(mapStateToProps, { updateStageProp, selectStageProp })(StagePropV2);
