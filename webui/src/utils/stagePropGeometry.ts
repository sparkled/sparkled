import { PixelPositions, StagePropType } from '@/src/types/viewModels'

export type Point = {
  x: number
  y: number
}

export type BoundingBox = {
  width: number
  height: number
}

export type StagePropGeometry = {
  /** A dense series of points used to draw the outline of the stage prop. */
  outlinePoints: Point[]

  /** The position of each LED, evenly distributed along the outline of the stage prop. */
  ledPoints: Point[]

  /** The bounding box of the unrotated, scaled stage prop, with its origin at (0, 0). */
  boundingBox: BoundingBox
}

const OUTLINE_SAMPLE_COUNT = 200
const RING_RADIUS = 50
const ARCH_RADIUS = 50
const SPIRAL_TURNS = 4
const SPIRAL_MAX_RADIUS = 50

/**
 * Builds the base (unscaled) outline points for a stage prop type. Points aren't guaranteed to start at
 * (0, 0); callers normalize the resulting outline against its actual bounds.
 */
function getBaseOutlinePoints(type: StagePropType): Point[] {
  switch (type) {
    case 'LINE':
      return sampleParametric(t => ({ x: t * 100, y: 0 }))
    case 'ARCH':
      return sampleParametric(t => {
        const angle = Math.PI + t * Math.PI
        return {
          x: ARCH_RADIUS + ARCH_RADIUS * Math.cos(angle),
          y: ARCH_RADIUS + ARCH_RADIUS * Math.sin(angle),
        }
      })
    case 'RING':
      return sampleParametric(t => {
        const angle = t * Math.PI * 2
        return {
          x: RING_RADIUS + RING_RADIUS * Math.cos(angle),
          y: RING_RADIUS + RING_RADIUS * Math.sin(angle),
        }
      })
    case 'SPIRAL':
      return sampleParametric(t => {
        const angle = t * SPIRAL_TURNS * Math.PI * 2
        const radius = t * SPIRAL_MAX_RADIUS
        return {
          x: SPIRAL_MAX_RADIUS + radius * Math.cos(angle),
          y: SPIRAL_MAX_RADIUS + radius * Math.sin(angle),
        }
      })
  }
}

function sampleParametric(fn: (t: number) => Point): Point[] {
  const points: Point[] = []
  for (let i = 0; i < OUTLINE_SAMPLE_COUNT; i++) {
    points.push(fn(i / (OUTLINE_SAMPLE_COUNT - 1)))
  }
  return points
}

function getSegmentLengths(points: Point[]): number[] {
  const cumulative: number[] = [0]
  for (let i = 1; i < points.length; i++) {
    const dx = points[i].x - points[i - 1].x
    const dy = points[i].y - points[i - 1].y
    cumulative.push(cumulative[i - 1] + Math.sqrt(dx * dx + dy * dy))
  }
  return cumulative
}

/**
 * Samples a fixed number of points, evenly distributed along the length of the given polyline. For closed
 * shapes (where the outline loops back on itself, e.g. a ring), the last point is treated as coincident
 * with the first, so LEDs are spaced around the full loop without doubling one up at the seam.
 */
function getEvenlySpacedPoints(points: Point[], count: number, closed: boolean): Point[] {
  if (count <= 0 || points.length === 0) {
    return []
  }

  const cumulativeLengths = getSegmentLengths(points)
  const totalLength = cumulativeLengths[cumulativeLengths.length - 1]

  if (count === 1) {
    return [getPointAtLength(points, cumulativeLengths, totalLength * 0.5)]
  }

  const divisor = closed ? count : count - 1
  const result: Point[] = []
  for (let i = 0; i < count; i++) {
    const targetLength = totalLength * (i / divisor)
    result.push(getPointAtLength(points, cumulativeLengths, targetLength))
  }
  return result
}

function getPointAtLength(
  points: Point[],
  cumulativeLengths: number[],
  targetLength: number,
): Point {
  let segmentIndex = cumulativeLengths.findIndex(length => length >= targetLength)
  if (segmentIndex <= 0) {
    segmentIndex = 1
  }

  const previousPoint = points[segmentIndex - 1]
  const nextPoint = points[segmentIndex]
  const previousLength = cumulativeLengths[segmentIndex - 1]
  const nextLength = cumulativeLengths[segmentIndex]
  const segmentLength = nextLength - previousLength
  const progress = segmentLength === 0 ? 0 : (targetLength - previousLength) / segmentLength

  return {
    x: previousPoint.x + (nextPoint.x - previousPoint.x) * progress,
    y: previousPoint.y + (nextPoint.y - previousPoint.y) * progress,
  }
}

function getBounds(points: Point[]): { minX: number; minY: number; maxX: number; maxY: number } {
  const xs = points.map(point => point.x)
  const ys = points.map(point => point.y)
  return {
    minX: Math.min(...xs),
    minY: Math.min(...ys),
    maxX: Math.max(...xs),
    maxY: Math.max(...ys),
  }
}

/** Whether a stage prop's outline is a closed loop (its end point coincides with its start point). */
function isClosedShape(type: StagePropType): boolean {
  return type === 'RING'
}

/**
 * Computes the outline, LED positions, and bounding box for a stage prop, given its type, scale, and LED
 * count. All coordinates are normalized so the shape's bounding box origin sits at (0, 0), regardless of
 * how the underlying shape formula happens to be defined.
 */
export function getStagePropGeometry(
  type: StagePropType,
  scaleX: number,
  scaleY: number,
  ledCount: number,
): StagePropGeometry {
  const scaledPoints = getBaseOutlinePoints(type).map(point => ({
    x: point.x * scaleX,
    y: point.y * scaleY,
  }))
  const bounds = getBounds(scaledPoints)
  const outlinePoints = scaledPoints.map(point => ({
    x: point.x - bounds.minX,
    y: point.y - bounds.minY,
  }))

  return {
    outlinePoints,
    ledPoints: getEvenlySpacedPoints(outlinePoints, ledCount, isClosedShape(type)),
    boundingBox: {
      width: bounds.maxX - bounds.minX,
      height: bounds.maxY - bounds.minY,
    },
  }
}

export type StagePropTransform = {
  type: StagePropType
  scaleX: number
  scaleY: number
  ledCount: number
  positionX: number
  positionY: number
  rotation: number
}

/**
 * Computes the absolute (stage-space) position of every LED on a stage prop, accounting for its position,
 * rotation, and scale. This mirrors the transform Pixi.js applies when rendering the stage prop container.
 */
export function computeLedPositions(stageProp: StagePropTransform): PixelPositions {
  const geometry = getStagePropGeometry(
    stageProp.type,
    stageProp.scaleX,
    stageProp.scaleY,
    stageProp.ledCount,
  )
  const pivotX = geometry.boundingBox.width / 2
  const pivotY = geometry.boundingBox.height / 2
  const centerX = stageProp.positionX + pivotX
  const centerY = stageProp.positionY + pivotY
  const angle = (stageProp.rotation * Math.PI) / 180
  const cos = Math.cos(angle)
  const sin = Math.sin(angle)

  const points = geometry.ledPoints.map(point => {
    const dx = point.x - pivotX
    const dy = point.y - pivotY
    return {
      x: centerX + dx * cos - dy * sin,
      y: centerY + dx * sin + dy * cos,
    }
  })

  if (points.length === 0) {
    return { points: [], bounds: { x1: 0, y1: 0, x2: 0, y2: 0 } }
  }

  const xs = points.map(point => point.x)
  const ys = points.map(point => point.y)
  return {
    points,
    bounds: {
      x1: Math.min(...xs),
      y1: Math.min(...ys),
      x2: Math.max(...xs),
      y2: Math.max(...ys),
    },
  }
}
