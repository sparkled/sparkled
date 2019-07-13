import _ from "lodash";
import * as PIXI from "pixi.js";
import React, {useState} from "react";
import {Point} from "svg-path-properties";
import * as StagePropPart from "./StagePropPart";

const lineWidth = 3;
const lineColor = 0xffffff;

declare interface Props {
  /** The parent container that manages the stage prop. */
  parent: PIXI.Container;

  /** The width of the stage prop in pixels, accounting for scale. */
  width: number;

  /** The height of the stage prop in pixels, accounting for scale. */
  height: number;

  /** A series of coordinates used to form the stage prop line. */
  points: Point[];
}

const StagePropPath: React.FC<Props> = props => {
  const [path, setPath] = useState<PIXI.Graphics | null>(null);

  if (path === null) {
    const stagePropPath = buildPath(props.points);
    setPath(stagePropPath);
    props.parent.addChild(stagePropPath);
  }

  return <></>;
};

function buildPath(points: Point[]) {
  const path = new PIXI.Graphics();
  path.name = StagePropPart.path.name;
  path.zIndex = StagePropPart.path.zIndex;
  path.lineStyle(lineWidth, lineColor);

  const origin = points[0];
  path.moveTo(origin.x, origin.y);
  _.forEach(points, point => path.lineTo(point.x, point.y));
  return path;
}

export default StagePropPath;
