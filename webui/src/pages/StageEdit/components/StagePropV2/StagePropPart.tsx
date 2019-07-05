declare interface StagePropPart {
  /** A unique name for a part of the stage prop. */
  name: string;

  /** The depth of the part, to ensure parts are drawn in the correct order. */
  zIndex: number;
}

/** Serves as an interactive later to facilitate moving the stage prop around by dragging it. */
export const background: StagePropPart = {name: "Background", zIndex: 0};
