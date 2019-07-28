export class StageViewModel {
  public id: number | null = null;
  public name: string = "";
  public width: number = 0;
  public height: number = 0;
  public stageProps: StagePropViewModel[] = [];
}

export interface StagePropViewModel {
  uuid: string;
  stageId: number;
  code: string;
  name: string;
  type: string;
  ledCount: number;
  reverse: boolean;
  positionX: number;
  positionY: number;
  scaleX: number;
  scaleY: number;
  rotation: number;
  brightness: number;
  displayOrder: number;
}
