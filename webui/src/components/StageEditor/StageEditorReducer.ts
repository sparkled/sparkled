import produce, {immerable} from "immer";
import {identity} from "lodash";
import React, {createContext, Dispatch} from "react";
import {StageViewModel} from "../../types/ViewModel";

export class State {
  private static [immerable] = true;
  public stage = new StageViewModel();
  public selectedStageProp = "";
}

export type Action =
  | { type: "SelectStageProp", payload: { uuid: string | null } }
  | { type: "MoveStageProp", payload: { x: number, y: number } }
  | { type: "RotateStageProp", payload: { rotation: number } };

export const StageEditorStateContext = createContext<State>(new State());
export const StageEditorDispatchContext = createContext<Dispatch<Action>>(identity);

export const stageEditorReducer: React.Reducer<State, Action> = (state, action): State => {
  return produce(state, draft => {
    switch (action.type) {
      case "SelectStageProp":
        draft.selectedStageProp = action.payload.uuid || "";
        break;

      case "MoveStageProp":
        moveStageProp(draft, action.payload.x, action.payload.y);
        break;

      case "RotateStageProp":
        rotateStageProp(draft, action.payload.rotation);
        break;
    }
  });
};

function moveStageProp(draft: State, x: number, y: number) {
  const stageProp = draft.stage.stageProps.find(sp => sp.uuid === draft.selectedStageProp);
  if (stageProp) {
    stageProp.positionX = x;
    stageProp.positionY = y;
  }
}

function rotateStageProp(draft: State, rotation: number) {
  const stageProp = draft.stage.stageProps.find(sp => sp.uuid === draft.selectedStageProp);
  if (stageProp) {
    stageProp.rotation = rotation;
  }
}
