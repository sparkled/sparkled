export interface StageViewModel {
  id: number | null;
  name: string | null;
  width: number | null;
  height: number | null;
  stageProps: StagePropViewModel[];
}

export interface StagePropViewModel {
  uuid: number | null;
  stageId: number | null;
  code: string | null;
  name: string | null;
  type: string | null;
  ledCount: number | null;
  reverse: boolean | null;
  positionX: number | null;
  positionY: number | null;
  scaleX: number | null;
  scaleY: number | null;
  rotation: number | null;
  brightness: number | null;
  displayOrder: number | null;
}
