import { Button, Grid, TextField } from '@material-ui/core'
import { find } from 'lodash'
import React, { useCallback, useContext, useEffect } from 'react'
import { useForm } from 'react-hook-form'
import {
  CODE_NAME,
  POSITIVE_INTEGER,
  POSITIVE_NUMBER
} from '../../../utils/regexes'
import {
  StageEditorDispatchContext,
  StageEditorStateContext
} from '../StageEditorReducer'

const StagePropDetails: React.FC = () => {
  const state = useContext(StageEditorStateContext)
  const dispatch = useContext(StageEditorDispatchContext)

  const { register, formState: { errors }, reset, setValue, watch } = useForm({
    mode: 'onChange'
  })

  const stageProp = find(state.stage.stageProps, {
    uuid: state.selectedStageProp
  })
  const hasStageProp = stageProp !== undefined

  useEffect(() => {
    if (!stageProp) {
      reset()
    } else {
      setValue('code', stageProp.code, { shouldValidate: true })
      setValue('name', stageProp.name, { shouldValidate: true })
      setValue('positionX', stageProp.positionX.toString(), { shouldValidate: true })
      setValue('positionY', stageProp.positionY.toString(), { shouldValidate: true })
      setValue('scaleX', stageProp.scaleX.toString(), { shouldValidate: true })
      setValue('scaleY', stageProp.scaleY.toString(), { shouldValidate: true })
      setValue('rotation', stageProp.rotation.toString(), { shouldValidate: true })
      setValue('ledCount', stageProp.ledCount.toString(), { shouldValidate: true })
      setValue('groupId', stageProp.groupId?.toString() ?? '', { shouldValidate: true })
      setValue('groupDisplayOrder', stageProp.groupDisplayOrder?.toString() ?? '', { shouldValidate: true })
    }
  }, [reset, setValue, stageProp])

  const [
    code,
    name,
    positionX,
    positionY,
    scaleX,
    scaleY,
    rotation,
    ledCount,
    groupId,
    groupDisplayOrder
  ] = watch([
    'code',
    'name',
    'positionX',
    'positionY',
    'scaleX',
    'scaleY',
    'rotation',
    'ledCount',
    'groupId',
    'groupDisplayOrder'
  ])

  useEffect(() => {
    if (code && !errors.code) {
      dispatch({
        type: 'UpdateStagePropCode',
        payload: code
      })
    }
  }, [code, dispatch, errors.code])

  useEffect(() => {
    if (name && !errors.name) {
      dispatch({
        type: 'UpdateStagePropName',
        payload: name
      })
    }
  }, [name, dispatch, errors.name])

  useEffect(() => {
    if (!errors.positionX && !errors.positionY) {
      dispatch({
        type: 'MoveStageProp',
        payload: {
          x: Number(positionX),
          y: Number(positionY)
        }
      })
    }
  }, [dispatch, errors.positionX, errors.positionY, positionX, positionY])

  useEffect(() => {
    if (!errors.scaleX && !errors.scaleY) {
      dispatch({
        type: 'ScaleStageProp',
        payload: {
          x: Number(scaleX),
          y: Number(scaleY)
        }
      })
    }
  }, [dispatch, errors.scaleX, errors.scaleY, scaleX, scaleY])

  useEffect(() => {
    if (!errors.rotation) {
      dispatch({
        type: 'RotateStageProp',
        payload: { rotation: Number(rotation) }
      })
    }
  }, [dispatch, errors.rotation, rotation])

  useEffect(() => {
    if (!errors.ledCount) {
      dispatch({
        type: 'UpdateStagePropLedCount',
        payload: { ledCount: Number(ledCount) }
      })
    }
  }, [dispatch, errors.ledCount, ledCount])

  useEffect(() => {
    if (!errors.groupId) {
      dispatch({
        type: 'UpdateStagePropGroupId',
        payload: groupId
      })
    }
  }, [dispatch, errors.groupId, groupId])

  useEffect(() => {
    if (!errors.groupDisplayOrder) {
      dispatch({
        type: 'UpdateStagePropGroupDisplayOrder',
        payload: groupDisplayOrder
      })
    }
  }, [dispatch, errors.groupDisplayOrder, groupDisplayOrder])

  const deleteStageProp = useCallback(() => {
    dispatch({ type: 'DeleteStageProp' })
  }, [dispatch])

  const codeField = register('code', { required: true, pattern: CODE_NAME })
  const nameField = register('name', { required: true })
  const positionXField = register('positionX', {
    required: true,
    min: 0,
    pattern: POSITIVE_INTEGER
  })
  const positionYField = register('positionY', {
    required: true,
    min: 0,
    pattern: POSITIVE_INTEGER
  })
  const scaleXField = register('scaleX', {
    required: true,
    min: 0.1,
    max: 10,
    pattern: POSITIVE_NUMBER
  })
  const scaleYField = register('scaleY', {
    required: true,
    min: 0.1,
    max: 10,
    pattern: POSITIVE_NUMBER
  })
  const rotationField = register('rotation', {
    required: true,
    min: 0,
    max: 359,
    pattern: POSITIVE_INTEGER
  })
  const ledCountField = register('ledCount', {
    required: true,
    min: 1,
    pattern: POSITIVE_INTEGER
  })
  const groupIdField = register('groupId', {})
  const groupDisplayOrderField = register('groupDisplayOrder', {
    min: 0,
    pattern: POSITIVE_INTEGER
  })

  return (
    <form>
      <Grid container spacing={2}>
        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Code"
            type="text"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.code !== undefined}
            { ...codeField }
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Name"
            type="text"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.name !== undefined}
            {...nameField}
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Position X"
            type="number"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.positionX !== undefined}
            {...positionXField}
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Position Y"
            type="number"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.positionY !== undefined}
            {...positionYField}
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Scale X"
            type="number"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.scaleX !== undefined}
            {...scaleXField}
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Scale Y"
            type="number"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.scaleY !== undefined}
            {...scaleYField}
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Rotation &deg;"
            type="number"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.rotation !== undefined}
            {...rotationField}
          />
        </Grid>
        <Grid item xs={6} />

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="LED Count"
            type="number"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.ledCount !== undefined}
            {...ledCountField}
          />
        </Grid>
        <Grid item xs={6} />

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Group ID"
            type="text"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.groupId !== undefined}
            {...groupIdField}
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            variant="outlined"
            label="Group Display Order"
            type="number"
            margin="dense"
            InputLabelProps={{ shrink: true }}
            disabled={!hasStageProp}
            error={errors.groupId !== undefined}
            {...groupDisplayOrderField}
          />
        </Grid>

        <Grid item xs={12}>
          <Button
            fullWidth
            variant="outlined"
            color="secondary"
            disabled={!hasStageProp}
            onClick={deleteStageProp}
          >
            Delete stage prop
          </Button>
        </Grid>
      </Grid>
    </form>
  )
}

export default StagePropDetails
