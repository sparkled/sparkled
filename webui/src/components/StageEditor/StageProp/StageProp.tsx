import _ from "lodash";
import * as PIXI from "pixi.js";
import React, {useEffect, useMemo, useState} from "react";
import {Point, SvgPathProperties, svgPathProperties} from "svg-path-properties";
import stagePropTypes from "../../../pages/StageEdit/stagePropTypes";
import {StagePropViewModel} from "../../../types/ViewModel";
import Logger from "../../../utils/Logger";
import StagePropBackground from "./StagePropBackground";
import StagePropPath from "./StagePropPath";
import StagePropRotateHandle from "./StagePropRotateHandle";

const logger = new Logger("StageProp");

interface Props {
  /** The PIXI application used to render the stage canvas. */
  pixiApp: PIXI.Application;

  /** The stage prop being displayed or edited. */
  stageProp: StagePropViewModel;

  /** Whether or not to enable stage prop editing. */
  editable: boolean;
}

interface State {
  /** The PIXI container that holds the stage prop. */
  pixiContainer: PIXI.Container;

  /** The width of the unscaled stage prop. */
  width: number;

  /** The height of the unscaled stage prop. */
  height: number;

  /** The scaled points of the line. */
  points: Point[];
}

const StageProp: React.FC<Props> = props => {
  const [state, setState] = useState<State | null>(null);

  useEffect(() => {
    const stagePropState = buildStageProp(props.pixiApp, props.stageProp);
    setState(stagePropState);
    return () => {
      logger.info(`Destroying ${props.stageProp.uuid}.`);
      stagePropState.pixiContainer.destroy({children: true});
      setState(null);
    };
  }, [props.pixiApp, props.stageProp]);

  // TODO recreate on update.
  if (state === null) {
    return <></>;
  } else {
    const {pixiContainer, width, height, points} = state;

    return (
      <>
        <StagePropBackground
          parent={pixiContainer}
          width={width}
          height={height}
          editable={props.editable}
          onClicked={selectStageProp}
          onMoved={moveStageProp}
        />

        <StagePropPath parent={pixiContainer} points={points} width={width} height={height}/>

        <StagePropRotateHandle
          parent={pixiContainer}
          width={width}
          editable={props.editable}
          onClicked={selectStageProp}
          onRotated={rotateStageProp}
        />
      </>
    );
  }
};

function buildStageProp(pixiApp: PIXI.Application, stageProp: StagePropViewModel): State {
  const {path} = stagePropTypes[stageProp.type!];
  const pathProperties = svgPathProperties(path);

  const points = getLinePoints(pathProperties, stageProp);
  const width = _.maxBy(points, "x")!.x;
  const height = _.maxBy(points, "y")!.y;

  const pixiContainer = buildContainer(stageProp, width, height);
  pixiApp.stage.addChild(pixiContainer);

  return {pixiContainer, points, width, height};
}

function selectStageProp() {
  // TODO this.props.selectStageProp(this.props.stageProp.uuid);
}

function moveStageProp(offsetX: number, offsetY: number) {
  // TODO
  // const { stageProp } = this.props;
  // this.props.updateStageProp({
  //   ...stageProp,
  //   positionX: Math.round(stageProp.positionX + offsetX),
  //   positionY: Math.round(stageProp.positionY + offsetY)
  // });
}

// TODO
function rotateStageProp(rotation: number) {
  // => this.props.updateStageProp({ ...this.props.stageProp, rotation });
}

function buildContainer(stageProp: StagePropViewModel, width: number, height: number) {
  const pixiContainer = new PIXI.Container();
  pixiContainer.sortableChildren = true;
  pixiContainer.position.x = stageProp.positionX! + (width / 2);
  pixiContainer.y = stageProp.positionY! + (height / 2);
  pixiContainer.rotation = stageProp.rotation! / 180 * Math.PI;
  pixiContainer.scale.x = pixiContainer.scale.y = 1;
  pixiContainer.pivot.x = width / 2;
  pixiContainer.pivot.y = height / 2;
  return pixiContainer;
}

function getLinePoints(pathProperties: SvgPathProperties, stageProp: StagePropViewModel): Point[] {
  const length = pathProperties.getTotalLength();

  const linePoints: Point[] = [];
  const pointCount = Math.floor(length);
  _.forEach(Array(pointCount + 1), (a, i) => {
    const progress = length * (i / pointCount);
    const point = pathProperties.getPointAtLength(progress);
    linePoints.push(point);
  });

  return _.map(linePoints, point => ({x: point.x * stageProp.scaleX!, y: point.y * stageProp.scaleY!}));
}

export default StageProp;
