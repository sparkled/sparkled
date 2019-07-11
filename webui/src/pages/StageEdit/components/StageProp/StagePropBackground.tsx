import * as PIXI from "pixi.js";
import React, {Dispatch, useState} from "react";
import Logger from "../../../../utils/Logger";
import * as StagePropPart from "./StagePropPart";

const logger = new Logger("StagePropBackground");
const padding = 5;

type InteractionEvent = PIXI.interaction.InteractionEvent;

interface Props {
  /** The parent container that manages the stage prop. */
  parent: PIXI.Container;

  /** The width of the stage prop in pixels, accounting for scale. */
  width: number;

  /** The height of the stage prop in pixels, accounting for scale. */
  height: number;

  /** Whether or not the drag handlers should be added. */
  editable: boolean;

  /** A callback to notify the stage prop that it has been clicked. */
  onClicked: () => void;

  /** A callback to notify the stage prop that it has been moved. */
  onMoved: (offsetX: number, offsetY: number) => void;
}

interface DragState {
  /** The X origin of the background when dragging begins. */
  originX: number;

  /** The Y origin of the background when dragging begins. */
  originY: number;

  /** The X position of the cursor relative to the pixi application when dragging begins. */
  dragStartX: number;

  /** The Y position of the cursor relative to the pixi application when dragging begins. */
  dragStartY: number;
}

const StagePropBackground: React.FC<Props> = props => {
  const [background, setBackground] = useState<PIXI.Graphics | null>(null);
  const [dragState, setDragState] = useState<DragState | null>(null);

  if (background === null) {
    const stagePropBackground = buildBackground(props);

    if (props.editable) {
      stagePropBackground.buttonMode = true;
      stagePropBackground.interactive = true;
      stagePropBackground.on("pointerdown", (event: InteractionEvent) => onDragStart(event, props, setDragState));
    }

    setBackground(stagePropBackground);
    props.parent.addChild(stagePropBackground);
  } else if (dragState !== null) {
    logger.debug("Dragging started, adding listeners.");

    background
      .on("pointermove", (event: InteractionEvent) => onDragMove(event, props, dragState))
      .on("pointerup", (event: InteractionEvent) => onDragEnd(event, props, dragState, setDragState))
      .on("pointerupoutside", (event: InteractionEvent) => onDragEnd(event, props, dragState, setDragState));
  } else {
    logger.debug("Dragging stopped, removing listeners.");

    background.removeListener("pointermove");
    background.removeListener("pointerup");
    background.removeListener("pointerupoutside");
  }

  return <></>;
};

function buildBackground(props: Props) {
  const background = new PIXI.Graphics();
  background.name = StagePropPart.background.name;
  background.zIndex = StagePropPart.background.zIndex;

  background.beginFill(0xFF00FF);
  background.drawRect(-padding, -padding, props.width + (2 * padding), props.height + (2 * padding));
  background.endFill();
  return background;
}

function onDragStart(event: InteractionEvent, props: Props, setDragState: Dispatch<DragState | null>) {
  const {onClicked, parent} = props;
  onClicked();

  const {x, y} = event.data.getLocalPosition(parent.parent);
  setDragState({originX: parent.x, originY: parent.y, dragStartX: x, dragStartY: y});
}

function onDragMove(event: InteractionEvent, props: Props, dragState: DragState) {
  const {parent} = props;

  if (dragState) {
    const {originX, originY, dragStartX, dragStartY} = dragState;

    // Convert the mouse/touch position to a relative offset.
    const {x: relativeParentX, y: relativeParentY} = event.data.getLocalPosition(parent.parent);

    // Add the new offset.
    parent.x = originX + (relativeParentX - dragStartX);
    parent.y = originY + (relativeParentY - dragStartY);
  }
}

function onDragEnd(event: InteractionEvent, props: Props, dragState: DragState, setDragState: Dispatch<null>) {
  const {x, y} = event.data.getLocalPosition(props.parent.parent);

  if (dragState) {
    const offsetX = x - dragState.dragStartX;
    const offsetY = y - dragState.dragStartY;
    setDragState(null);
    props.onMoved(offsetX, offsetY);
  }
}

export default StagePropBackground;
