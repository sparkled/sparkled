import produce from "immer";
import React, {createContext, Dispatch} from "react";
import {StageViewModel} from "../../types/ViewModel";

export interface State {
  stage: StageViewModel;
  selectedStageProps: Set<string>;
}

export type Action =
  | { type: "SelectStageProp", payload: { uuid: string | null }; };

export const StateContext = createContext<State | null>(null);

export const DispatchContext = createContext<Dispatch<Action>>(() => {/* No-op. */});

export const reducer: React.Reducer<State, Action> = (state, action): State => {
  return produce(state, draft => {
    if (action.type === "SelectStageProp") {
      selectStageProp(draft, action.payload.uuid);
    }
  });
};

function selectStageProp(state: State, uuid: string | null) {
  state.selectedStageProps = uuid === null ? new Set<string>() : new Set<string>(uuid);
}
