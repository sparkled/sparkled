import _ from 'lodash';
import * as PIXI from "pixi.js";
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { svgPathProperties } from "svg-path-properties";
import Logger from '../../../../utils/Logger';
import { selectStageProp, updateStageProp } from '../../actions';
import stagePropTypes from '../../stagePropTypes';
import StagePropBackground from './StagePropBackground';
import StagePropPath from './StagePropPath';
import StagePropRotateHandle from './StagePropRotateHandle';

const logger = new Logger('StageProp');

class StagePropV2 extends Component {

  state = {
    pixiContainer: null,
    dragState: null
  };

  componentDidMount() {
    this.buildStageProp();
  }

  async componentDidUpdate(prevProps) {
    if (this.state.pixiContainer && this.props.stageProp !== prevProps.stageProp) {
      await this.destroyContainer();
      this.buildStageProp();
    }
  }

  render() {
    const { pixiContainer, points, width, height } = this.state;
    return !pixiContainer ? <></> : (
      <>
        <StagePropBackground parent={pixiContainer} width={width} height={height}
                             onClicked={this.selectStageProp} onMoved={this.moveStageProp}/>

        <StagePropPath parent={pixiContainer} points={points} width={width} height={height}/>

        <StagePropRotateHandle parent={pixiContainer} points={points} width={width}
                               onClicked={this.selectStageProp} onRotated={this.rotateStageProp}/>
      </>
    );
  }

  async componentWillUnmount() {
    logger.debug('Destroying.');
    await this.destroyContainer();
  }

  selectStageProp = () => {
    this.props.selectStageProp(this.props.stageProp.uuid);
  }

  moveStageProp = (offsetX, offsetY) => {
    const { stageProp } = this.props;
    this.props.updateStageProp({
      ...stageProp,
      positionX: Math.round(stageProp.positionX + offsetX),
      positionY: Math.round(stageProp.positionY + offsetY)
    });
  }

  rotateStageProp = rotation => this.props.updateStageProp({ ...this.props.stageProp, rotation });

  buildStageProp() {
    const { pixiApp, stageProp } = this.props;

    const { path, segments } = stagePropTypes[stageProp.type];
    const pathProperties = svgPathProperties(path);

    const points = this.getLinePoints(pathProperties, stageProp, segments);
    const width = _.maxBy(points, 'x').x;
    const height = _.maxBy(points, 'y').y;

    const pixiContainer = this.buildContainer(stageProp, width, height);
    pixiApp.stage.addChild(pixiContainer);

    this.setState({ pixiContainer, points, width, height });
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
