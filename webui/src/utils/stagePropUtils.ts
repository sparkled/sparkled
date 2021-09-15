import { Point, SvgPathProperties } from 'svg-path-properties'
import { StagePropViewModel } from '../types/ViewModel'
import _ from 'lodash'

function getLinePoints(
  pathProperties: SvgPathProperties,
  stageProp: StagePropViewModel,
  pointCount: number = -1
): Point[] {
  const length = pathProperties.getTotalLength()
  pointCount = pointCount === -1 ? Math.floor(length) : pointCount

  const linePoints: Point[] = []

  if (pointCount === 1) {
    linePoints.push(pathProperties.getPointAtLength(length * .5))
  } else {
    _.forEach(Array(pointCount), (a, i) => {
      const progress = length * (i / (pointCount - 1))
      const point = pathProperties.getPointAtLength(progress)
      linePoints.push(point)
    })
  }

  return _.map(linePoints, point => ({
    x: point.x * (stageProp.scaleX ?? 1),
    y: point.y * (stageProp.scaleY ?? 1)
  }))
}

export {
  getLinePoints
}
