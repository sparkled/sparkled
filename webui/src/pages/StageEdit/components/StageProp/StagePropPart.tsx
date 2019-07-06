declare interface StagePropPart {
  /** A unique name for a part of the stage prop. */
  name: string;

  /** The depth of the part, to ensure parts are drawn in the correct order. */
  zIndex: number;
}

/** Serves as an interactive layer to facilitate moving the stage prop around by dragging it. */
export const background: StagePropPart = {name: "Background", zIndex: 0};

/** The line representing the physical stage prop object. */
export const path: StagePropPart = {name: "Path", zIndex: 1};

/** An interactive handle that rotates the stage prop when moved by the user. */
export const rotateHandle: StagePropPart = {name: "RotateHandle", zIndex: 2};
