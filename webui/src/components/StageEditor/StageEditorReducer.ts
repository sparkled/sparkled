import produce, {immerable} from "immer";
import {identity, remove} from "lodash";
import React, {createContext, Dispatch} from "react";
import {StageViewModel} from "../../types/ViewModel";
import {StagePropType} from "../../pages/StageEdit/stagePropTypes";
import uuidv4 from "uuid/v4";

export class State {
  private static [immerable] = true;
  public stage = new StageViewModel();
  public selectedStageProp = "";
}

export type Action =
  | { type: "SelectStageProp", payload: { uuid: string | null } }
  | { type: "AddStageProp", payload: { type: StagePropType } }
  | { type: "MoveStageProp", payload: { x: number, y: number } }
  | { type: "ScaleStageProp", payload: { x: number, y: number } }
  | { type: "RotateStageProp", payload: { rotation: number } }
  | { type: "DeleteStageProp" };

export const StageEditorStateContext = createContext<State>(new State());
export const StageEditorDispatchContext = createContext<Dispatch<Action>>(identity);

export const stageEditorReducer: React.Reducer<State, Action> = (state, action): State => {
  return produce(state, draft => {
    switch (action.type) {
      case "SelectStageProp":
        draft.selectedStageProp = action.payload.uuid || "";
        break;
      case "AddStageProp":
        addStageProp(draft, action.payload.type);
        break;
      case "MoveStageProp":
        moveStageProp(draft, action.payload.x, action.payload.y);
        break;
      case "ScaleStageProp":
        scaleStageProp(draft, action.payload.x, action.payload.y);
        break;
      case "RotateStageProp":
        rotateStageProp(draft, action.payload.rotation);
        break;
      case "DeleteStageProp":
        deleteStageProp(draft);
        break;
    }
  });
};

function addStageProp(draft: State, type: StagePropType) {
  const {stage} = draft;

  stage.stageProps.push({
    uuid: uuidv4(),
    stageId: stage.id!,
    code: `PROP_${stage.stageProps.length}`,
    name: `Prop ${stage.stageProps.length}`,
    type: type.id,
    ledCount: 10,
    reverse: false,
    positionX: 0,
    positionY: 0,
    scaleX: 1,
    scaleY: 1,
    rotation: 0,
    brightness: 100,
    displayOrder: 0
  });

  stage.stageProps.forEach((stageProp, i) => stageProp.displayOrder = i);
}

function moveStageProp(draft: State, x: number, y: number) {
  const stageProp = draft.stage.stageProps.find(sp => sp.uuid === draft.selectedStageProp);
  if (stageProp) {
    stageProp.positionX = x;
    stageProp.positionY = y;
  }
}

function scaleStageProp(draft: State, x: number, y: number) {
  const stageProp = draft.stage.stageProps.find(sp => sp.uuid === draft.selectedStageProp);
  if (stageProp) {
    stageProp.scaleX = x;
    stageProp.scaleY = y;
  }
}

function rotateStageProp(draft: State, rotation: number) {
  const stageProp = draft.stage.stageProps.find(sp => sp.uuid === draft.selectedStageProp);
  if (stageProp) {
    stageProp.rotation = rotation;
  }
}

function deleteStageProp(draft: State) {
  remove(draft.stage.stageProps, sp => sp.uuid === draft.selectedStageProp);
}
