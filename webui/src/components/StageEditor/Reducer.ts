import produce from "immer";
import {identity} from "lodash";
import React, {createContext, Dispatch} from "react";
import {StageViewModel} from "../../types/ViewModel";

export interface State {
  stage: StageViewModel;
  selectedStageProp: string;
}

export type Action =
  | { type: "SelectStageProp", payload: { uuid: string | null } }
  | { type: "MoveStageProp", payload: { x: number, y: number } }
  | { type: "RotateStageProp", payload: { rotation: number } };

export const StateContext = createContext<State>({
  stage: new StageViewModel(),
  selectedStageProp: ""
});

export const DispatchContext = createContext<Dispatch<Action>>(identity);

export const reducer: React.Reducer<State, Action> = (state, action): State => {
  return produce(state, draft => {
    if (action.type === "SelectStageProp") {
      selectStageProp(draft, action.payload.uuid);
    } else if (action.type === "MoveStageProp") {
      const {x, y} = action.payload;
      moveStageProp(draft, x, y);
    } else if (action.type === "RotateStageProp") {
      rotateStageProp(draft, action.payload.rotation);
    }
  });
};

function selectStageProp(state: State, uuid: string | null) {
  state.selectedStageProp = uuid || "";
}

function moveStageProp(state: State, x: number, y: number) {
  const stageProp = state.stage.stageProps.find(sp => sp.uuid === state.selectedStageProp);
  if (stageProp) {
    stageProp.positionX = x;
    stageProp.positionY = y;
  }
}

function rotateStageProp(state: State, rotation: number) {
  const stageProp = state.stage.stageProps.find(sp => sp.uuid === state.selectedStageProp);
  if (stageProp) {
    stageProp.rotation = rotation;
  }
}
