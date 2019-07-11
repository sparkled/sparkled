import _ from "lodash";
import * as PIXI from "pixi.js";
import React, {Dispatch, useEffect, useRef, useState} from "react";
import {StageViewModel} from "../../../../types/ViewModel";
import Logger from "../../../../utils/Logger";
import {clamp} from "../../../../utils/numberUtils";
import StageProp from "../StageProp";

const logger = new Logger("StageCanvas");
const gridSize = 20;
const zoomLimits = {min: .5, max: 5};

type InteractionEvent = PIXI.interaction.InteractionEvent;

interface Props {
  /** The stage being viewed or edited. */
  stage: StageViewModel;

  /** Whether or not to enable stage prop editing. */
  editable: boolean;
}

interface DragState {
  /** The X origin of the background when dragging begins. */
  originX: number;

  /** The Y origin of the background when dragging begins. */
  originY: number;

  /** The X position of the cursor when dragging begins. */
  mouseX: number;

  /** The Y position of the cursor when dragging begins. */
  mouseY: number;
}

const StageCanvas: React.FC<Props> = props => {
  const canvasElement = useRef<HTMLDivElement>(null);
  const [pixiApp, setPixiApp] = useState<PIXI.Application | null>(null);
  const [dragState, setDragState] = useState<DragState | null>(null);

  useEffect(() => {
    const resolution = window.devicePixelRatio || 1;
    const app = new PIXI.Application({resolution, antialias: true, transparent: true});
    setPixiApp(app);
    return () => app.destroy(true);
  }, []);

  if (pixiApp === null || canvasElement.current === null) {
    logger.debug("Waiting for canvas element to mount.");
  } else if (pixiApp.stage.children.length === 0) {
    initCanvas(pixiApp, canvasElement.current);

    const {interaction} = pixiApp.renderer.plugins;
    interaction.on("pointerdown", (event: InteractionEvent) => onDragStart(event, pixiApp, setDragState));
    pixiApp.view.addEventListener("wheel", event => onZoom(event, pixiApp));
  } else if (dragState !== null) {
    pixiApp.renderer.plugins.interaction
      .on("pointermove", (event: InteractionEvent) => onDragMove(event, pixiApp, dragState))
      .on("pointerup", () => onDragEnd(setDragState))
      .on("pointerupoutside", () => onDragEnd(setDragState));
  } else {
    pixiApp.renderer.plugins.interaction.removeListener("pointermove");
    pixiApp.renderer.plugins.interaction.removeListener("pointerup");
    pixiApp.renderer.plugins.interaction.removeListener("pointerupoutside");
  }

  return (
    <div ref={canvasElement}>
      {renderStageProps(pixiApp, props)}
    </div>
  );
};

function initCanvas(pixiApp: PIXI.Application, container: HTMLDivElement): PIXI.Application {
  const parent = container.parentElement!;
  pixiApp.resizeTo = parent;
  pixiApp.view.style.width = pixiApp.view.style.height = "100%";
  pixiApp.stage.addChild(buildGrid());
  parent.appendChild(pixiApp.view);

  redrawGrid(pixiApp);
  return pixiApp;
}

function buildGrid() {
  const grid = new PIXI.Graphics();
  grid.position.x = grid.position.y = 0;
  grid.name = "Grid";
  return grid;
}

function onZoom(event: WheelEvent, pixiApp: PIXI.Application) {
  const newScale = clamp(pixiApp.stage.scale.x + (event.deltaY / -30), zoomLimits.min, zoomLimits.max);
  pixiApp.stage.scale.x = pixiApp.stage.scale.y = newScale;

  redrawGrid(pixiApp);
}

function onDragStart(event: InteractionEvent, pixiApp: PIXI.Application, setDragState: Dispatch<DragState>) {
  // Only drag if the background is selected (i.e. no stage prop is set as the target).
  if (event.target === null) {
    const {clientX, clientY, changedTouches} = event.data.originalEvent as MouseEvent & TouchEvent;
    const mouseX = clientX !== undefined ? clientX : changedTouches[0].clientX;
    const mouseY = clientY !== undefined ? clientY : changedTouches[0].clientY;

    setDragState({originX: pixiApp.stage.x, originY: pixiApp.stage.y, mouseX, mouseY});
  }
}

function onDragMove(event: InteractionEvent, pixiApp: PIXI.Application, dragState: DragState | null) {
  if (!dragState) {
    return;
  }

  const {originX, originY, mouseX, mouseY} = dragState;

  const {clientX, clientY, changedTouches} = event.data.originalEvent as MouseEvent & TouchEvent;
  const newMouseX = clientX !== undefined ? clientX : changedTouches[0].clientX;
  const newMouseY = clientY !== undefined ? clientY : changedTouches[0].clientY;

  pixiApp.stage.x = originX + (newMouseX - mouseX);
  pixiApp.stage.y = originY + (newMouseY - mouseY);
  redrawGrid(pixiApp);
}

function onDragEnd(setDragState: Dispatch<null>) {
  setDragState(null);
}

function redrawGrid(pixiApp: PIXI.Application) {
  const grid = pixiApp.stage.getChildByName("Grid") as PIXI.Graphics;
  grid.clear();

  const scale = pixiApp.stage.scale.x;

  // Calculate left and top positions for the grid, and subtract gridSize to allow for partial grid cells to be drawn.
  let startX = (-pixiApp.stage.x / scale) - gridSize;
  let startY = (-pixiApp.stage.y / scale) - gridSize;

  // Adjust positions to account for the grid offset, otherwise the grid will always be drawn from the top-left.
  startX -= startX % gridSize;
  startY -= startY % gridSize;

  // Calculate the width and height, and add gridSize to allow for partial grid cells to be drawn.
  const width = pixiApp.view.clientWidth / scale + gridSize * 2;
  const height = pixiApp.view.clientHeight / scale + gridSize * 2;

  grid.lineStyle(1, 0xffffff, .2, 1);

  for (let x = 0; x < width; x += gridSize) {
    grid.moveTo(x + startX, startY);
    grid.lineTo(x + startX, height + startY);
  }

  for (let y = 0; y < height; y += gridSize) {
    grid.moveTo(startX, y + startY);
    grid.lineTo(width + startX, y + startY);
  }
}

function renderStageProps(pixiApp: PIXI.Application | null, props: Props) {
  const {stage, editable} = props;

  if (!pixiApp) {
    return <></>;
  }

  return _.map(stage.stageProps, stageProp => {
    // Keying on the stage prop type will recreate the component with the correct path when its type changes.
    const key = stageProp.uuid! + stageProp.type!;
    return <StageProp key={key} stageProp={stageProp} pixiApp={pixiApp} editable={editable}/>;
  });
}

export default StageCanvas;
