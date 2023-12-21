import { produce } from 'immer'
import _, { identity, isEqual, remove } from 'lodash'
import React, { createContext, Dispatch } from 'react'
import { StagePropType } from '../../data/stagePropTypes'
import { PixelPositions, StagePropViewModel, StageViewModel } from '../../types/viewModels.ts'
import { uniqueId } from '../../utils/idUtils'

type State = {
  stage: StageViewModel
  selectedStageProp: string
}

export type Action =
  | { type: 'SelectStageProp'; payload: { id: string | null } }
  | { type: 'AddStageProp'; payload: { type: StagePropType } }
  | { type: 'UpdateStagePropCode'; payload: string }
  | { type: 'UpdateStagePropName'; payload: string }
  | { type: 'MoveStageProp'; payload: { x: number; y: number } }
  | { type: 'ScaleStageProp'; payload: { x: number; y: number } }
  | { type: 'RotateStageProp'; payload: { rotation: number } }
  | { type: 'UpdateStagePropLedCount'; payload: { ledCount: number } }
  | { type: 'UpdateStagePropLedPositions'; payload: { ledPositions: PixelPositions } }
  | { type: 'UpdateStagePropGroupCode'; payload: string | undefined }
  | { type: 'UpdateStagePropGroupDisplayOrder'; payload: string | null }
  | { type: 'UpdateStageProp'; payload: Partial<StagePropViewModel> }
  | { type: 'DeleteStageProp' }

export const StageEditorStateContext = createContext<State>({
  stage: {
    id: '',
    stageProps: [],
    name: '',
    width: 0,
    height: 0,
  },
  selectedStageProp: '',
})

export const StageEditorDispatchContext = createContext<Dispatch<Action>>(identity)

export const stageEditorReducer: React.Reducer<State, Action> = (state, action): State => {
  return produce(state, draft => {
    const selectedStageProp = getSelectedStageProp(draft)

    switch (action.type) {
      case 'SelectStageProp':
        draft.selectedStageProp = action.payload.id || ''
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
            selectedStageProp.ledPositions = action.payload.ledPositions
          }
        }
        break
      case 'UpdateStagePropGroupCode':
        updateStagePropGroupCode(selectedStageProp, action.payload)
        break
      case 'UpdateStagePropGroupDisplayOrder':
        updateStagePropGroupDisplayOrder(selectedStageProp, action.payload)
        break
      case 'UpdateStageProp':
        if (selectedStageProp) {
          _.keys(action.payload).forEach(key => {
            (selectedStageProp as any)[key] = (action.payload as any)[key]
          })
        }
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
    id: uniqueId(),
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
    groupCode: undefined,
    groupDisplayOrder: undefined,
    ledPositions: { bounds: { x1: 0, y1: 0, x2: 0, y2: 0 }, points: [] },
  })

  stage.stageProps.forEach((stageProp, i) => (stageProp.displayOrder = i))
}

function moveStageProp(stageProp: StagePropViewModel | undefined, x: number, y: number) {
  if (stageProp) {
    stageProp.positionX = x
    stageProp.positionY = y
  }
}

function scaleStageProp(stageProp: StagePropViewModel | undefined, x: number, y: number) {
  if (stageProp) {
    stageProp.scaleX = x
    stageProp.scaleY = y
  }
}

function rotateStageProp(stageProp: StagePropViewModel | undefined, rotation: number) {
  if (stageProp) {
    stageProp.rotation = rotation
  }
}

function updateStagePropLedCount(stageProp: StagePropViewModel | undefined, ledCount: number) {
  if (stageProp) {
    stageProp.ledCount = ledCount
  }
}

function updateStagePropGroupCode(stageProp: StagePropViewModel | undefined, groupCode: string | undefined) {
  if (stageProp) {
    stageProp.groupCode = groupCode || undefined
  }
}

function updateStagePropGroupDisplayOrder(stageProp: StagePropViewModel | undefined, groupDisplayOrder: string | null) {
  if (stageProp) {
    let number: number | undefined = parseInt(groupDisplayOrder ?? '')
    number = isNaN(number) ? undefined : number
    stageProp.groupDisplayOrder = number
  }
}

const getSelectedStageProp = function (draft: State) {
  return draft.stage.stageProps.find(sp => sp.id === draft.selectedStageProp)
}

function deleteStageProp(draft: State) {
  remove(draft.stage.stageProps, sp => sp.id === draft.selectedStageProp)
}
