import * as PIXI from "pixi.js";
import React, {Dispatch, useState} from "react";
import Logger from "../../../../utils/Logger";
import * as StagePropPart from "./StagePropPart";

const logger = new Logger("StagePropRotateHandle");
const handleRadius = 6;
const quarterTurn = Math.PI / 2;
const fullCircle = Math.PI * 2;

declare type InteractionEvent = PIXI.interaction.InteractionEvent;

declare interface Props {
  /** The parent container that manages the stage prop. */
  parent: PIXI.Container;

  /** The width of the stage prop in pixels, accounting for scale. */
  width: number;

  /** A callback to notify the stage prop that it has been clicked. */
  onClicked: () => void;

  /** A callback to notify the stage prop that it has been rotated. */
  onRotated: (rotation: number) => void;
}

declare interface DragState {
  /** The X origin of the background when dragging begins. */
  originX: number;

  /** The Y origin of the background when dragging begins. */
  originY: number;
}

const StagePropRotateHandle: React.FC<Props> = props => {
  const [rotateHandle, setRotateHandle] = useState<PIXI.Graphics | null>(null);
  const [dragState, setDragState] = useState<DragState | null>(null);

  if (rotateHandle === null) {
    const stagePropRotateHandle = buildRotateHandle(props);
    stagePropRotateHandle.on("pointerdown", (event: InteractionEvent) => onDragStart(event, props, setDragState));
    setRotateHandle(stagePropRotateHandle);
    props.parent.addChild(stagePropRotateHandle);
  } else if (dragState !== null) {
    logger.debug("Dragging started, adding listeners.");

    rotateHandle
      .on("pointermove", (event: InteractionEvent) => onDragMove(event, props, rotateHandle, dragState))
      .on("pointerup", (event: InteractionEvent) => onDragEnd(event, props, dragState, setDragState))
      .on("pointerupoutside", (event: InteractionEvent) => onDragEnd(event, props, dragState, setDragState));
  } else {
    logger.debug("Dragging stopped, removing listeners.");

    rotateHandle.removeListener("pointermove");
    rotateHandle.removeListener("pointerup");
    rotateHandle.removeListener("pointerupoutside");
  }

  return <></>;
};

function buildRotateHandle(props: Props) {
  const rotateHandle = new PIXI.Graphics();
  rotateHandle.name = StagePropPart.rotateHandle.name;
  rotateHandle.zIndex = StagePropPart.rotateHandle.zIndex;

  rotateHandle.beginFill(0x00FF00);
  rotateHandle.drawCircle(0, 0, handleRadius);
  rotateHandle.endFill();
  rotateHandle.x = props.width / 2;
  rotateHandle.y = handleRadius * -4;

  rotateHandle.buttonMode = true;
  rotateHandle.interactive = true;
  return rotateHandle;
}

function onDragStart(event: InteractionEvent, props: Props, setDragState: Dispatch<DragState | null>) {
  const {onClicked, parent} = props;
  onClicked();
  setDragState({originX: parent.x, originY: parent.y});
}

function onDragMove(event: InteractionEvent, props: Props, rotateHandle: PIXI.Graphics, dragState: DragState) {
  const {parent} = props;
  const {originX, originY} = dragState;

  // Convert the mouse/touch position to a relative offset.
  const {x: relativeParentX, y: relativeParentY} = event.data.getLocalPosition(parent.parent);

  // Update the rotation
  rotateHandle.position.y = event.data.getLocalPosition(parent).y;

  // Pixi's rotation starts at North, but atan2 starts at East, so need to rotate back one quarter.
  let newRotation = Math.atan2(originY - relativeParentY, originX - relativeParentX) - quarterTurn;
  newRotation %= fullCircle;
  newRotation += newRotation < 0 ? fullCircle : 0;
  parent.rotation = newRotation;
}

function onDragEnd(event: InteractionEvent, props: Props, dragState: DragState, setDragState: Dispatch<null>) {
  const rotation = Math.round(props.parent.rotation / Math.PI * 180);
  setDragState(null);
  props.onRotated(rotation);
}

export default StagePropRotateHandle;
