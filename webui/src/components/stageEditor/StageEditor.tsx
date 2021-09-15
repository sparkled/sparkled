import { Theme } from '@material-ui/core'
import { makeStyles } from '@material-ui/styles'
import _ from 'lodash'
import * as PIXI from 'pixi.js'
import React, {
  Dispatch,
  useCallback,
  useEffect,
  useMemo,
  useReducer,
  useRef,
  useState
} from 'react'
import SimpleBar from 'simplebar-react'
import 'simplebar/dist/simplebar.css'
import { StageViewModel } from '../../types/ViewModel'
import Logger from '../../utils/Logger'
import { clamp } from '../../utils/numberUtils'
import EditorSidebar from './EditorSidebar'
import {
  StageEditorDispatchContext,
  stageEditorReducer,
  StageEditorStateContext
} from './StageEditorReducer'
import StageProp from './StageProp'

const logger = new Logger('StageEditor')
const gridSize = 20
const zoomLimits = { min: 0.5, max: 5 }

type InteractionEvent = PIXI.interaction.InteractionEvent

interface Props extends React.HTMLAttributes<HTMLElement> {
  /** The stage being viewed or edited. */
  stage: StageViewModel

  /** An optional callback to invoke whenever a change is made to the stage. */
  onStageUpdate?: (stage: StageViewModel) => any // TODO use.

  /** Whether or not to display the tools sidebar. */
  toolsVisible?: boolean

  /** Whether or not to enable stage prop editing. */
  editable: boolean
}

interface DragState {
  /** The X origin of the background when dragging begins. */
  originX: number

  /** The Y origin of the background when dragging begins. */
  originY: number

  /** The X position of the cursor when dragging begins. */
  mouseX: number

  /** The Y position of the cursor when dragging begins. */
  mouseY: number
}

const sidebarWidth = 300

const useStyles = makeStyles((theme: Theme) => {
  const tween = 'cubic-bezier(0.4, 0, 0.2, 1)'

  return {
    sidebarContainer: {
      position: 'absolute',
      top: 0,
      right: 0,
      width: sidebarWidth,
      maxHeight: '100%',
      transition: `opacity .5s ${tween}, transform .5s ${tween}`
    },
    sidebar: {
      margin: theme.spacing(1)
    },
    noTools: {
      opacity: 0,
      transform: `translate3d(${sidebarWidth}px, 0, 0)`
    }
  }
})

const StageEditor: React.FC<Props> = props => {
  const classes = useStyles()

  const canvasElement = useRef<HTMLDivElement>(null)
  const [pixiApp, setPixiApp] = useState<PIXI.Application | null>(null)
  const [dragState, setDragState] = useState<DragState | null>(null)

  const [state, dispatch] = useReducer(stageEditorReducer, {
    stage: props.stage,
    selectedStageProp: ''
  })

  const deselectStageProp = useCallback(
    () => dispatch({ type: 'SelectStageProp', payload: { uuid: null } }),
    []
  )

  useEffect(() => {
    logger.info('Creating.')
    const resolution = Math.max(2, window.devicePixelRatio)
    const app = new PIXI.Application({
      resolution,
      antialias: true,
      transparent: true
    })
    setPixiApp(app)
    return () => {
      logger.info('Destroying.')
      app.destroy(true)
    }
  }, [])

  const onStageUpdate = useCallback(props.onStageUpdate || _.identity, [])
  useEffect(() => {
    logger.info('Stage changed.')
    onStageUpdate(state.stage)
  }, [onStageUpdate, state.stage])

  if (pixiApp === null || canvasElement.current === null) {
    logger.debug('Waiting for canvas element to mount.')
  } else if (pixiApp.stage.children.length === 0) {
    initCanvas(pixiApp, canvasElement.current, props.stage)

    const { interaction } = pixiApp.renderer.plugins
    interaction.on('pointerdown', (event: InteractionEvent) => {
      onDragStart(event, pixiApp, deselectStageProp, setDragState)
    })
    pixiApp.view.addEventListener('wheel', event => onZoom(event, pixiApp))
  } else if (dragState !== null) {
    pixiApp.renderer.plugins.interaction
      .on('pointermove', (event: InteractionEvent) =>
        onDragMove(event, pixiApp, dragState)
      )
      .on('pointerup', () => onDragEnd(setDragState))
      .on('pointerupoutside', () => onDragEnd(setDragState))
  } else {
    pixiApp.renderer.plugins.interaction.removeListener('pointermove')
    pixiApp.renderer.plugins.interaction.removeListener('pointerup')
    pixiApp.renderer.plugins.interaction.removeListener('pointerupoutside')
  }

  const stageProps = useMemo(() => {
    return renderStageProps(pixiApp, state.stage, props.editable)
  }, [pixiApp, props.editable, state.stage])

  const toolsClass = props.toolsVisible ? '' : classes.noTools
  return (
    <StageEditorStateContext.Provider value={state}>
      <StageEditorDispatchContext.Provider value={dispatch}>
        <SimpleBar className={`${classes.sidebarContainer} ${toolsClass}`}>
          <EditorSidebar className={classes.sidebar} />
        </SimpleBar>

        <div ref={canvasElement}>{stageProps}</div>
      </StageEditorDispatchContext.Provider>
    </StageEditorStateContext.Provider>
  )
}

function initCanvas(
  pixiApp: PIXI.Application,
  container: HTMLDivElement,
  stage: StageViewModel,
): PIXI.Application {
  const parent = container.parentElement!
  pixiApp.resizeTo = parent
  pixiApp.view.style.width = pixiApp.view.style.height = '100%'
  pixiApp.stage.addChild(buildGrid())
  parent.appendChild(pixiApp.view)

  pixiApp.stage.x = ((parent.clientWidth - sidebarWidth) / 2) - (stage.width / 2)
  pixiApp.stage.y = (parent.clientHeight / 2) - (stage.height / 2)

  renderGrid(pixiApp, stage)
  return pixiApp
}

function buildGrid() {
  const grid = new PIXI.Graphics()
  grid.position.x = grid.position.y = 0
  grid.name = 'Grid'
  return grid
}

function onZoom(event: WheelEvent, pixiApp: PIXI.Application) {
  const scrollDirection = Math.sign(event.deltaY)
  const newScale = clamp(
    pixiApp.stage.scale.x + scrollDirection / -50,
    zoomLimits.min,
    zoomLimits.max
  )

  pixiApp.stage.scale.x = pixiApp.stage.scale.y = newScale
}

function onDragStart(
  event: InteractionEvent,
  pixiApp: PIXI.Application,
  deselectStageProp: () => void,
  setDragState: Dispatch<DragState>
) {
  // Only drag if the background is selected (i.e. no stage prop is set as the target).
  if (event.target === null) {
    deselectStageProp()
    const { clientX, clientY, changedTouches } = event.data
      .originalEvent as MouseEvent & TouchEvent
    const mouseX = clientX !== undefined ? clientX : changedTouches[0].clientX
    const mouseY = clientY !== undefined ? clientY : changedTouches[0].clientY

    setDragState({
      originX: pixiApp.stage.x,
      originY: pixiApp.stage.y,
      mouseX,
      mouseY
    })
  }
}

function onDragMove(
  event: InteractionEvent,
  pixiApp: PIXI.Application,
  dragState: DragState | null
) {
  if (!dragState) {
    return
  }

  const { originX, originY, mouseX, mouseY } = dragState
  const { clientX, clientY, changedTouches } = event.data
    .originalEvent as MouseEvent & TouchEvent
  const newMouseX = clientX !== undefined ? clientX : changedTouches[0].clientX
  const newMouseY = clientY !== undefined ? clientY : changedTouches[0].clientY

  pixiApp.stage.x = originX + (newMouseX - mouseX)
  pixiApp.stage.y = originY + (newMouseY - mouseY)
}

function onDragEnd(setDragState: Dispatch<null>) {
  setDragState(null)
}

function renderGrid(pixiApp: PIXI.Application, stage: StageViewModel) {
  const grid = pixiApp.stage.getChildByName('Grid') as PIXI.Graphics
  grid.clear()

  // Calculate left and top positions for the grid, and subtract gridSize to allow for partial grid cells to be drawn.
  let startX = 0
  let startY = 0

  grid.lineStyle(1, 0xffffff, 0.2, .5)

  for (let x = 0; x < stage.width; x += gridSize) {
    grid.moveTo(x + startX, startY)
    grid.lineTo(x + startX, stage.height + startY)
  }

  for (let y = 0; y < stage.height; y += gridSize) {
    grid.moveTo(startX, y + startY)
    grid.lineTo(stage.width + startX, y + startY)
  }

  // Right line
  grid.moveTo(stage.width, 0)
  grid.lineTo(stage.width, stage.height)

  // Bottom line
  grid.moveTo(0, stage.height)
  grid.lineTo(stage.width, stage.height)
}

function renderStageProps(
  pixiApp: PIXI.Application | null,
  stage: StageViewModel,
  editable: boolean
) {
  if (!pixiApp) {
    return <></>
  }

  return _.map(stage.stageProps, stageProp => {
    // Keying on type will recreate the component with the correct path when its type changes.
    // Keying on scale rebuilds the prop line data whenever the prop is resized.
    const key = `${stageProp.uuid!}:${stageProp.type!}:${stageProp.scaleX}:${stageProp.scaleY}:${stageProp.ledCount}`
    return (
      <StageProp
        key={key}
        stageProp={stageProp}
        pixiApp={pixiApp}
        editable={editable}
      />
    )
  })
}

export default StageEditor
