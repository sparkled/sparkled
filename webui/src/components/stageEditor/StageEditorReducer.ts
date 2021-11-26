import produce, { immerable } from 'immer'
import { identity, isEqual, remove } from 'lodash'
import React, { createContext, Dispatch } from 'react'
import { Point, StagePropViewModel, StageViewModel } from '../../types/ViewModel'
import { StagePropType } from '../../data/stagePropTypes'
import uuidv4 from 'uuid/v4'

export class State {
  private static [immerable] = true
  public stage = new StageViewModel()
  public selectedStageProp = ''
}

export type Action =
  | { type: 'SelectStageProp'; payload: { uuid: string | null } }
  | { type: 'AddStageProp'; payload: { type: StagePropType } }
  | { type: 'UpdateStagePropCode'; payload: string }
  | { type: 'UpdateStagePropName'; payload: string }
  | { type: 'MoveStageProp'; payload: { x: number; y: number } }
  | { type: 'ScaleStageProp'; payload: { x: number; y: number } }
  | { type: 'RotateStageProp'; payload: { rotation: number } }
  | { type: 'UpdateStagePropLedCount'; payload: { ledCount: number } }
  | { type: 'UpdateStagePropLedPositions'; payload: { ledPositions: Point[] } }
  | { type: 'UpdateStagePropGroupId'; payload: string | null }
  | { type: 'UpdateStagePropGroupDisplayOrder'; payload: string | null }
  | { type: 'DeleteStageProp' }

export const StageEditorStateContext = createContext<State>(new State())
export const StageEditorDispatchContext = createContext<Dispatch<Action>>(
  identity
)

export const stageEditorReducer: React.Reducer<State, Action> = (
  state,
  action
): State => {
  return produce(state, draft => {
    const selectedStageProp = getSelectedStageProp(draft)

    switch (action.type) {
      case 'SelectStageProp':
        draft.selectedStageProp = action.payload.uuid || ''
        break
      case 'AddStageProp':
        addStageProp(draft, action.payload.type)
        break
      case 'UpdateStagePropCode':
        if (selectedStageProp) {
          selectedStageProp.code = action.payload
        }
        break
      case 'UpdateStagePropName':
        if (selectedStageProp) {
          selectedStageProp.name = action.payload
        }
        break
      case 'MoveStageProp':
        moveStageProp(selectedStageProp, action.payload.x, action.payload.y)
        break
      case 'ScaleStageProp':
        scaleStageProp(selectedStageProp, action.payload.x, action.payload.y)
        break
      case 'RotateStageProp':
        rotateStageProp(selectedStageProp, action.payload.rotation)
        break
      case 'UpdateStagePropLedCount':
        updateStagePropLedCount(selectedStageProp, action.payload.ledCount)
        break
      case 'UpdateStagePropLedPositions':
        if (selectedStageProp) {
          if (!isEqual(selectedStageProp.ledPositions, action.payload.ledPositions)) {
            selectedStageProp.ledPositions = action.payload.ledPositions;
          }
        }
        break
      case 'UpdateStagePropGroupId':
        updateStagePropGroupId(selectedStageProp, action.payload)
        break
      case 'UpdateStagePropGroupDisplayOrder':
        updateStagePropGroupDisplayOrder(selectedStageProp, action.payload)
        break
      case 'DeleteStageProp':
        deleteStageProp(draft)
        break
    }
  })
}

function addStageProp(draft: State, type: StagePropType) {
  const { stage } = draft

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
    displayOrder: 0,
    groupId: null,
    groupDisplayOrder: null,
    ledPositions: [],
  })

  stage.stageProps.forEach((stageProp, i) => (stageProp.displayOrder = i))
}

function moveStageProp(
  stageProp: StagePropViewModel | undefined,
  x: number,
  y: number
) {
  if (stageProp) {
    stageProp.positionX = x
    stageProp.positionY = y
  }
}

function scaleStageProp(
  stageProp: StagePropViewModel | undefined,
  x: number,
  y: number
) {
  if (stageProp) {
    stageProp.scaleX = x
    stageProp.scaleY = y
  }
}

function rotateStageProp(
  stageProp: StagePropViewModel | undefined,
  rotation: number
) {
  if (stageProp) {
    stageProp.rotation = rotation
  }
}

function updateStagePropLedCount(
  stageProp: StagePropViewModel | undefined,
  ledCount: number
) {
  if (stageProp) {
    stageProp.ledCount = ledCount
  }
}

function updateStagePropGroupId(
  stageProp: StagePropViewModel | undefined,
  groupId: string | null
) {
  if (stageProp) {
    stageProp.groupId = groupId || null
  }
}

function updateStagePropGroupDisplayOrder(
  stageProp: StagePropViewModel | undefined,
  groupDisplayOrder: string | null
) {
  if (stageProp) {
    let number: number | null = parseInt(groupDisplayOrder ?? '')
    number = isNaN(number) ? null : number
    stageProp.groupDisplayOrder = number
  }
}

let getSelectedStageProp = function(draft: State) {
  return draft.stage.stageProps.find(sp => sp.uuid === draft.selectedStageProp)
}

function deleteStageProp(draft: State) {
  remove(draft.stage.stageProps, sp => sp.uuid === draft.selectedStageProp)
}
