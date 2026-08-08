'use client'

import { StagePropViewModel, StageViewModel } from '@/src/types/viewModels'
import { StagePropGeometry, getStagePropGeometry } from '@/src/utils/stagePropGeometry'
import * as PIXI from 'pixi.js'
import { useEffect, useRef } from 'react'

export type StagePropCanvasProps = {
  stage: StageViewModel
  selectedStagePropId: string | null
  onSelectStageProp: (id: string | null) => void
  onMoveStageProp: (id: string, positionX: number, positionY: number) => void
  onRotateStageProp: (id: string, rotation: number) => void
  onResizeStageProp: (
    id: string,
    positionX: number,
    positionY: number,
    scaleX: number,
    scaleY: number,
  ) => void
}

const STAGE_FILL_COLOR = 0x27272a
const STAGE_BORDER_COLOR = 0x3f3f46
const OUTLINE_COLOR = 0xa1a1aa
const SELECTED_OUTLINE_COLOR = 0x3b82f6
const LED_COLOR = 0x18181b
const LED_BORDER_COLOR = 0xd4d4d8
const SELECTED_LED_BORDER_COLOR = 0x3b82f6
const HANDLE_COLOR = 0xffffff
const HANDLE_BORDER_COLOR = 0x3b82f6
const OUTLINE_WIDTH = 3
const LED_RADIUS = 4
const HANDLE_RADIUS = 6
const RESIZE_HANDLE_SIZE = 8
const ROTATE_HANDLE_GAP = 24
const HIT_PADDING = 10
const MIN_ZOOM = 0.1
const MAX_ZOOM = 4
const MIN_STAGE_PROP_SCALE = 0.1
const MIN_STAGE_PROP_SIZE = 4

type ResizeHandleId =
  | 'top-left'
  | 'top'
  | 'top-right'
  | 'right'
  | 'bottom-right'
  | 'bottom'
  | 'bottom-left'
  | 'left'

type ResizeHandleDef = {
  id: ResizeHandleId
  fx: number
  fy: number
  cursor: string
}

const RESIZE_HANDLE_DEFS: ResizeHandleDef[] = [
  { id: 'top-left', fx: 0, fy: 0, cursor: 'nwse-resize' },
  { id: 'top', fx: 0.5, fy: 0, cursor: 'ns-resize' },
  { id: 'top-right', fx: 1, fy: 0, cursor: 'nesw-resize' },
  { id: 'right', fx: 1, fy: 0.5, cursor: 'ew-resize' },
  { id: 'bottom-right', fx: 1, fy: 1, cursor: 'nwse-resize' },
  { id: 'bottom', fx: 0.5, fy: 1, cursor: 'ns-resize' },
  { id: 'bottom-left', fx: 0, fy: 1, cursor: 'nesw-resize' },
  { id: 'left', fx: 0, fy: 0.5, cursor: 'ew-resize' },
]

type PropNode = {
  container: PIXI.Container
  background: PIXI.Graphics
  outline: PIXI.Graphics
  leds: PIXI.Graphics
  rotateHandleLine: PIXI.Graphics
  rotateHandle: PIXI.Graphics
  resizeHandles: Map<ResizeHandleId, PIXI.Graphics>
  geometryKey: string
  pivot: { x: number; y: number }
  boundingBox: { width: number; height: number }
  handleOffset: { x: number; y: number; angle: number }
}

type DragState =
  | { kind: 'move'; id: string; originX: number; originY: number; startX: number; startY: number }
  | { kind: 'rotate'; id: string; angleOffset: number }
  | { kind: 'pan'; startX: number; startY: number; originX: number; originY: number }
  | {
      kind: 'resize'
      id: string
      handleFx: number
      handleFy: number
      rotation: number
      originContainerX: number
      originContainerY: number
      originWidth: number
      originHeight: number
      originScaleX: number
      originScaleY: number
    }

/**
 * Renders the stage and its stage props using Pixi.js. Stage props can be dragged to reposition them,
 * rotated by dragging the handle above the top centre of their bounding box, or resized by dragging one of
 * the edge/corner handles around their bounding box.
 */
export function StagePropCanvas({
  stage,
  selectedStagePropId,
  onSelectStageProp,
  onMoveStageProp,
  onRotateStageProp,
  onResizeStageProp,
}: StagePropCanvasProps) {
  const containerRef = useRef<HTMLDivElement>(null)
  const appRef = useRef<PIXI.Application | null>(null)
  const worldRef = useRef<PIXI.Container | null>(null)
  const stageBackgroundRef = useRef<PIXI.Graphics | null>(null)
  const propNodesRef = useRef<Map<string, PropNode>>(new Map())
  const dragStateRef = useRef<DragState | null>(null)
  const callbacksRef = useRef({
    onSelectStageProp,
    onMoveStageProp,
    onRotateStageProp,
    onResizeStageProp,
  })
  const stageSizeRef = useRef({ width: stage.width, height: stage.height })
  const stagePropsRef = useRef(stage.stageProps)

  callbacksRef.current = { onSelectStageProp, onMoveStageProp, onRotateStageProp, onResizeStageProp }
  stageSizeRef.current = { width: stage.width, height: stage.height }
  stagePropsRef.current = stage.stageProps

  // Set up the Pixi application once on mount.
  useEffect(() => {
    const container = containerRef.current
    if (!container) {
      return
    }

    let destroyed = false
    const app = new PIXI.Application()
    const wheelHandlerRef: { current: ((event: WheelEvent) => void) | null } = { current: null }

    const world = new PIXI.Container()
    const stageBackground = new PIXI.Graphics()
    world.addChild(stageBackground)

    async function init() {
      await app.init({
        backgroundAlpha: 0,
        antialias: true,
        resizeTo: container!,
        resolution: window.devicePixelRatio || 1,
        autoDensity: true,
      })

      if (destroyed) {
        app.destroy(true, { children: true })
        return
      }

      container!.appendChild(app.canvas)
      app.stage.addChild(world)
      app.stage.eventMode = 'static'
      app.stage.hitArea = app.screen

      centerWorld(app, world, stageSizeRef.current)

      app.stage.on('pointerdown', event => {
        if (event.target === app.stage) {
          callbacksRef.current.onSelectStageProp(null)

          dragStateRef.current = {
            kind: 'pan',
            startX: event.global.x,
            startY: event.global.y,
            originX: world.x,
            originY: world.y,
          }
        }
      })

      app.stage.on('globalpointermove', event => {
        const dragState = dragStateRef.current
        if (!dragState) {
          return
        }

        if (dragState.kind === 'pan') {
          world.x = dragState.originX + (event.global.x - dragState.startX)
          world.y = dragState.originY + (event.global.y - dragState.startY)
          return
        }

        const node = propNodesRef.current.get(dragState.id)
        if (!node) {
          return
        }

        const local = world.toLocal(event.global)

        if (dragState.kind === 'move') {
          node.container.x = dragState.originX + (local.x - dragState.startX)
          node.container.y = dragState.originY + (local.y - dragState.startY)
        } else if (dragState.kind === 'rotate') {
          const dx = local.x - node.container.x
          const dy = local.y - node.container.y
          let rotation = Math.atan2(dy, dx) - dragState.angleOffset
          rotation = normalizeAngle(rotation)
          node.container.rotation = rotation
          node.rotateHandle.rotation = -rotation
        } else if (dragState.kind === 'resize') {
          const stageProp = stagePropsRef.current.find(prop => prop.id === dragState.id)
          if (!stageProp) {
            return
          }

          const scaleFromCenter = event.getModifierState('Alt')
          const freeScale = event.getModifierState('Shift')

          const cos = Math.cos(dragState.rotation)
          const sin = Math.sin(dragState.rotation)
          const relX = local.x - dragState.originContainerX
          const relY = local.y - dragState.originContainerY
          const localOffsetX = relX * cos + relY * sin
          const localOffsetY = -relX * sin + relY * cos

          const originPivotX = dragState.originWidth / 2
          const originPivotY = dragState.originHeight / 2
          const pointerBoxX = originPivotX + localOffsetX
          const pointerBoxY = originPivotY + localOffsetY

          const anchorFx = scaleFromCenter ? 0.5 : 1 - dragState.handleFx
          const anchorFy = scaleFromCenter ? 0.5 : 1 - dragState.handleFy
          const anchorOldX = dragState.originWidth * anchorFx
          const anchorOldY = dragState.originHeight * anchorFy

          const rawWidth = Math.abs(pointerBoxX - anchorOldX)
          const rawHeight = Math.abs(pointerBoxY - anchorOldY)

          const { originWidth, originHeight } = dragState
          const isTopOrBottomHandle = dragState.handleFx === 0.5
          const isLeftOrRightHandle = dragState.handleFy === 0.5

          let newWidth = originWidth
          let newHeight = originHeight

          if (isTopOrBottomHandle) {
            newHeight = rawHeight
            if (!freeScale && originHeight > 0) {
              newWidth = originWidth * (newHeight / originHeight)
            }
          } else if (isLeftOrRightHandle) {
            newWidth = rawWidth
            if (!freeScale && originWidth > 0) {
              newHeight = originHeight * (newWidth / originWidth)
            }
          } else if (freeScale) {
            newWidth = rawWidth
            newHeight = rawHeight
          } else {
            const widthRatio = originWidth > 0 ? rawWidth / originWidth : 0
            const heightRatio = originHeight > 0 ? rawHeight / originHeight : 0
            const ratio = Math.max(widthRatio, heightRatio) || 1
            newWidth = originWidth > 0 ? originWidth * ratio : newWidth
            newHeight = originHeight > 0 ? originHeight * ratio : newHeight
          }

          newWidth = Math.max(newWidth, MIN_STAGE_PROP_SIZE)
          newHeight = Math.max(newHeight, MIN_STAGE_PROP_SIZE)

          // Shapes with a degenerate width/height (e.g. a straight line has no height) keep that axis'
          // scale untouched, since there's nothing meaningful to measure a ratio against.
          const newScaleX =
            originWidth > 0
              ? Math.max(dragState.originScaleX * (newWidth / originWidth), MIN_STAGE_PROP_SCALE)
              : dragState.originScaleX
          const newScaleY =
            originHeight > 0
              ? Math.max(dragState.originScaleY * (newHeight / originHeight), MIN_STAGE_PROP_SCALE)
              : dragState.originScaleY

          const geometry = getStagePropGeometry(
            stageProp.type,
            newScaleX,
            newScaleY,
            stageProp.ledCount,
          )
          const newPivotX = geometry.boundingBox.width / 2
          const newPivotY = geometry.boundingBox.height / 2

          const dxOld = anchorOldX - originPivotX
          const dyOld = anchorOldY - originPivotY
          const worldAnchorX = dragState.originContainerX + dxOld * cos - dyOld * sin
          const worldAnchorY = dragState.originContainerY + dxOld * sin + dyOld * cos

          const anchorNewX = geometry.boundingBox.width * anchorFx
          const anchorNewY = geometry.boundingBox.height * anchorFy
          const dxNew = anchorNewX - newPivotX
          const dyNew = anchorNewY - newPivotY

          node.container.pivot.set(newPivotX, newPivotY)
          node.container.position.set(
            worldAnchorX - (dxNew * cos - dyNew * sin),
            worldAnchorY - (dxNew * sin + dyNew * cos),
          )

          drawGeometry(node, geometry, true)
        }
      })

      const endDrag = () => {
        const dragState = dragStateRef.current
        if (!dragState || dragState.kind === 'pan') {
          dragStateRef.current = null
          return
        }

        const node = propNodesRef.current.get(dragState.id)
        if (node) {
          if (dragState.kind === 'move') {
            const positionX = Math.round(node.container.x - node.pivot.x)
            const positionY = Math.round(node.container.y - node.pivot.y)
            callbacksRef.current.onMoveStageProp(dragState.id, positionX, positionY)
          } else if (dragState.kind === 'rotate') {
            const rotationDegrees = Math.round((node.container.rotation * 180) / Math.PI) % 360
            callbacksRef.current.onRotateStageProp(dragState.id, rotationDegrees)
          } else if (dragState.kind === 'resize') {
            // Shapes with a degenerate width/height (e.g. a straight line has no height) never have that
            // axis' scale touched during the drag, so it's carried over unchanged here too.
            const scaleX =
              dragState.originWidth > 0
                ? Math.round(
                    (node.boundingBox.width / (dragState.originWidth / dragState.originScaleX)) * 100,
                  ) / 100
                : dragState.originScaleX
            const scaleY =
              dragState.originHeight > 0
                ? Math.round(
                    (node.boundingBox.height / (dragState.originHeight / dragState.originScaleY)) * 100,
                  ) / 100
                : dragState.originScaleY
            const positionX = Math.round(node.container.x - node.pivot.x)
            const positionY = Math.round(node.container.y - node.pivot.y)
            callbacksRef.current.onResizeStageProp(dragState.id, positionX, positionY, scaleX, scaleY)
            node.geometryKey = ''
          }
        }

        dragStateRef.current = null
      }

      app.stage.on('pointerup', endDrag)
      app.stage.on('pointerupoutside', endDrag)

      const handleWheel = (event: WheelEvent) => {
        event.preventDefault()
        const zoomFactor = event.deltaY < 0 ? 1.1 : 1 / 1.1
        const newScale = clamp(world.scale.x * zoomFactor, MIN_ZOOM, MAX_ZOOM)

        const rect = app.canvas.getBoundingClientRect()
        const pointer = { x: event.clientX - rect.left, y: event.clientY - rect.top }
        const before = world.toLocal(pointer)
        world.scale.set(newScale)
        const after = world.toLocal(pointer)
        world.x += (after.x - before.x) * newScale
        world.y += (after.y - before.y) * newScale
      }

      app.canvas.addEventListener('wheel', handleWheel, { passive: false })
      wheelHandlerRef.current = handleWheel

      appRef.current = app
      worldRef.current = world
      stageBackgroundRef.current = stageBackground

      drawStageBackground(stageBackground, stageSizeRef.current)
      syncStageProps()
    }

    void init()

    return () => {
      destroyed = true
      appRef.current = null
      worldRef.current = null
      stageBackgroundRef.current = null
      propNodesRef.current.clear()
      if (app.renderer) {
        if (wheelHandlerRef.current) {
          app.canvas.removeEventListener('wheel', wheelHandlerRef.current)
        }
        app.destroy(true, { children: true })
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  // Redraw the stage background whenever the stage dimensions change.
  useEffect(() => {
    const app = appRef.current
    const world = worldRef.current
    const stageBackground = stageBackgroundRef.current
    if (!app || !world || !stageBackground) {
      return
    }

    drawStageBackground(stageBackground, { width: stage.width, height: stage.height })
    centerWorld(app, world, { width: stage.width, height: stage.height })
  }, [stage.width, stage.height])

  // Synchronize the stage prop Pixi nodes whenever the stage props or selection changes.
  useEffect(() => {
    syncStageProps()
  })

  function syncStageProps() {
    const app = appRef.current
    const world = worldRef.current
    if (!app || !world) {
      return
    }

    const nodes = propNodesRef.current
    const currentIds = new Set(stage.stageProps.map(stageProp => stageProp.id))

    Array.from(nodes.entries()).forEach(([id, node]) => {
      if (!currentIds.has(id)) {
        world.removeChild(node.container)
        node.container.destroy({ children: true })
        nodes.delete(id)
      }
    })

    stage.stageProps.forEach(stageProp => {
      let node = nodes.get(stageProp.id)
      if (!node) {
        node = createPropNode(stageProp.id)
        nodes.set(stageProp.id, node)
        world.addChild(node.container)
      }

      updatePropNode(node, stageProp, stageProp.id === selectedStagePropId)
    })
  }

  function createPropNode(id: string): PropNode {
    const container = new PIXI.Container()
    const background = new PIXI.Graphics()
    const outline = new PIXI.Graphics()
    const leds = new PIXI.Graphics()
    const rotateHandleLine = new PIXI.Graphics()
    const rotateHandle = new PIXI.Graphics()
    const resizeHandles = new Map<ResizeHandleId, PIXI.Graphics>()

    container.addChild(background, outline, leds, rotateHandleLine, rotateHandle)

    background.eventMode = 'static'
    background.cursor = 'grab'
    background.on('pointerdown', event => {
      event.stopPropagation()
      callbacksRef.current.onSelectStageProp(id)

      const world = worldRef.current
      if (!world) {
        return
      }

      const local = world.toLocal(event.global)
      dragStateRef.current = {
        kind: 'move',
        id,
        originX: container.x,
        originY: container.y,
        startX: local.x,
        startY: local.y,
      }
    })

    rotateHandle.eventMode = 'static'
    rotateHandle.cursor = 'grab'
    rotateHandle.on('pointerdown', event => {
      event.stopPropagation()
      callbacksRef.current.onSelectStageProp(id)

      const node = propNodesRef.current.get(id)
      if (!node) {
        return
      }

      dragStateRef.current = {
        kind: 'rotate',
        id,
        angleOffset: node.handleOffset.angle,
      }
    })

    RESIZE_HANDLE_DEFS.forEach(def => {
      const handle = new PIXI.Graphics()
      handle.eventMode = 'static'
      handle.cursor = def.cursor
      handle.on('pointerdown', event => {
        event.stopPropagation()
        callbacksRef.current.onSelectStageProp(id)

        const node = propNodesRef.current.get(id)
        const stageProp = stagePropsRef.current.find(prop => prop.id === id)
        if (!node || !stageProp) {
          return
        }

        dragStateRef.current = {
          kind: 'resize',
          id,
          handleFx: def.fx,
          handleFy: def.fy,
          rotation: node.container.rotation,
          originContainerX: node.container.x,
          originContainerY: node.container.y,
          originWidth: node.boundingBox.width,
          originHeight: node.boundingBox.height,
          originScaleX: stageProp.scaleX,
          originScaleY: stageProp.scaleY,
        }
      })

      container.addChild(handle)
      resizeHandles.set(def.id, handle)
    })

    return {
      container,
      background,
      outline,
      leds,
      rotateHandleLine,
      rotateHandle,
      resizeHandles,
      geometryKey: '',
      pivot: { x: 0, y: 0 },
      boundingBox: { width: 0, height: 0 },
      handleOffset: { x: 0, y: 0, angle: 0 },
    }
  }

  function updatePropNode(node: PropNode, stageProp: StagePropViewModel, isSelected: boolean) {
    const geometryKey = `${stageProp.type}:${stageProp.scaleX}:${stageProp.scaleY}:${stageProp.ledCount}:${isSelected}`

    const dragState = dragStateRef.current
    const isResizingThisNode = dragState?.kind === 'resize' && dragState.id === stageProp.id

    if (node.geometryKey !== geometryKey && !isResizingThisNode) {
      const geometry = getStagePropGeometry(
        stageProp.type,
        stageProp.scaleX,
        stageProp.scaleY,
        stageProp.ledCount,
      )
      drawGeometry(node, geometry, isSelected)
      node.geometryKey = geometryKey
    }

    // Don't stomp on an in-progress drag/rotate/resize for this node.
    const isDraggingThisNode =
      dragState && dragState.kind !== 'pan' && dragState.id === stageProp.id

    if (!isDraggingThisNode) {
      node.container.x = stageProp.positionX + node.pivot.x
      node.container.y = stageProp.positionY + node.pivot.y
      node.container.rotation = (stageProp.rotation * Math.PI) / 180
      node.rotateHandle.rotation = -node.container.rotation
    }
  }

  return <div ref={containerRef} className='h-full w-full touch-none' />
}

function drawStageBackground(graphics: PIXI.Graphics, size: { width: number; height: number }) {
  graphics.clear()
  graphics
    .rect(0, 0, size.width, size.height)
    .fill(STAGE_FILL_COLOR)
    .stroke({ width: 2, color: STAGE_BORDER_COLOR })

  const gridSize = 50
  for (let x = gridSize; x < size.width; x += gridSize) {
    graphics.moveTo(x, 0).lineTo(x, size.height)
  }
  for (let y = gridSize; y < size.height; y += gridSize) {
    graphics.moveTo(0, y).lineTo(size.width, y)
  }
  graphics.stroke({ width: 1, color: STAGE_BORDER_COLOR, alpha: 0.5 })
}

/**
 * Redraws a stage prop's outline, LEDs, and handles for the given geometry, updating the container's pivot
 * to match. Used both when a stage prop's committed geometry changes and to preview an in-progress resize.
 */
function drawGeometry(node: PropNode, geometry: StagePropGeometry, isSelected: boolean) {
  const pivotX = geometry.boundingBox.width / 2
  const pivotY = geometry.boundingBox.height / 2

  node.pivot = { x: pivotX, y: pivotY }
  node.boundingBox = { width: geometry.boundingBox.width, height: geometry.boundingBox.height }
  node.container.pivot.set(pivotX, pivotY)

  const outlineColor = isSelected ? SELECTED_OUTLINE_COLOR : OUTLINE_COLOR
  const ledBorderColor = isSelected ? SELECTED_LED_BORDER_COLOR : LED_BORDER_COLOR
  const handleAlpha = isSelected ? 1 : 0

  node.background.clear()
  node.background
    .rect(
      -HIT_PADDING,
      -HIT_PADDING,
      geometry.boundingBox.width + HIT_PADDING * 2,
      geometry.boundingBox.height + HIT_PADDING * 2,
    )
    .fill({ color: 0xffffff, alpha: 0.001 })

  if (isSelected) {
    node.background.stroke({ width: 1, color: SELECTED_OUTLINE_COLOR, alpha: 0.5 })
  }

  node.outline.clear()
  if (geometry.outlinePoints.length > 1) {
    node.outline.moveTo(geometry.outlinePoints[0].x, geometry.outlinePoints[0].y)
    geometry.outlinePoints.slice(1).forEach(point => node.outline.lineTo(point.x, point.y))
    node.outline.stroke({
      width: OUTLINE_WIDTH,
      color: outlineColor,
      cap: 'round',
      join: 'round',
    })
  }

  node.leds.clear()
  geometry.ledPoints.forEach(point => {
    node.leds
      .circle(point.x, point.y, LED_RADIUS)
      .fill(LED_COLOR)
      .stroke({ width: 1.5, color: ledBorderColor })
  })

  // Rotate handle: sits above the top centre of the bounding box, connected by a line.
  const rotateHandleX = pivotX
  const rotateHandleY = -(HIT_PADDING + ROTATE_HANDLE_GAP)
  const handleOffsetX = rotateHandleX - pivotX
  const handleOffsetY = rotateHandleY - pivotY
  node.handleOffset = {
    x: handleOffsetX,
    y: handleOffsetY,
    angle: Math.atan2(handleOffsetY, handleOffsetX),
  }

  node.rotateHandleLine.clear()
  node.rotateHandleLine
    .moveTo(rotateHandleX, -HIT_PADDING)
    .lineTo(rotateHandleX, rotateHandleY)
    .stroke({ width: 1.5, color: HANDLE_BORDER_COLOR, alpha: handleAlpha })
  node.rotateHandleLine.visible = isSelected

  node.rotateHandle.clear()
  node.rotateHandle
    .circle(0, 0, HANDLE_RADIUS)
    .fill(isSelected ? HANDLE_COLOR : 0x000000)
    .stroke({ width: 2, color: isSelected ? HANDLE_BORDER_COLOR : 0x000000, alpha: handleAlpha })
  node.rotateHandle.position.set(rotateHandleX, rotateHandleY)
  node.rotateHandle.visible = isSelected
  node.rotateHandle.eventMode = isSelected ? 'static' : 'none'

  // Resize handles: one at each corner and edge midpoint of the bounding box.
  RESIZE_HANDLE_DEFS.forEach(def => {
    const handle = node.resizeHandles.get(def.id)
    if (!handle) {
      return
    }

    const handleX = -HIT_PADDING + def.fx * (geometry.boundingBox.width + HIT_PADDING * 2)
    const handleY = -HIT_PADDING + def.fy * (geometry.boundingBox.height + HIT_PADDING * 2)

    handle.clear()
    handle
      .rect(-RESIZE_HANDLE_SIZE / 2, -RESIZE_HANDLE_SIZE / 2, RESIZE_HANDLE_SIZE, RESIZE_HANDLE_SIZE)
      .fill(isSelected ? HANDLE_COLOR : 0x000000)
      .stroke({ width: 2, color: isSelected ? HANDLE_BORDER_COLOR : 0x000000, alpha: handleAlpha })
    handle.position.set(handleX, handleY)
    handle.visible = isSelected
    handle.eventMode = isSelected ? 'static' : 'none'
  })
}

function centerWorld(
  app: PIXI.Application,
  world: PIXI.Container,
  size: { width: number; height: number },
) {
  const padding = 60
  const availableWidth = Math.max(app.screen.width - padding * 2, 1)
  const availableHeight = Math.max(app.screen.height - padding * 2, 1)
  const scale = Math.min(1, availableWidth / size.width, availableHeight / size.height)

  world.scale.set(scale)
  world.x = (app.screen.width - size.width * scale) / 2
  world.y = (app.screen.height - size.height * scale) / 2
}

function normalizeAngle(angle: number): number {
  const twoPi = Math.PI * 2
  let normalized = angle % twoPi
  if (normalized < 0) {
    normalized += twoPi
  }
  return normalized
}

function clamp(value: number, min: number, max: number): number {
  return Math.min(max, Math.max(min, value))
}
