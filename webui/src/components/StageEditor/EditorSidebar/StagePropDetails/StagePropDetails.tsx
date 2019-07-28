import {Grid, TextField} from "@material-ui/core";
import {find, isEmpty} from "lodash";
import React, {HTMLAttributes, useContext, useEffect} from "react";
import useForm from "react-hook-form";
import {POSITIVE_INTEGER} from "../../../../utils/regexes";
import {DispatchContext, StateContext} from "../../Reducer";

const StagePropDetails: React.FC<HTMLAttributes<void>> = props => {
  const state = useContext(StateContext);
  const dispatch = useContext(DispatchContext);

  const {register, errors, reset, setValue, watch} = useForm({mode: "onChange"});

  const stageProp = find(state.stage.stageProps, {uuid: state.selectedStageProp});
  const hasStageProp = stageProp !== undefined;

  useEffect(() => {
    if (!stageProp) {
      reset();
    } else {
      setValue("positionX", stageProp.positionX.toString(), true);
      setValue("positionY", stageProp.positionY.toString(), true);
      setValue("rotation", stageProp.rotation.toString(), true);
    }
  }, [reset, setValue, stageProp]);

  const [positionX, positionY, rotation] = watch(["positionX", "positionY", "rotation"]);
  useEffect(() => {
    const hasValues = !isEmpty(positionX) && !isEmpty(positionY);
    const isValid = !errors.positionX && !errors.positionY;
    if (hasValues && isValid) {
      dispatch({
        type: "MoveStageProp",
        payload: {
          x: Number(positionX),
          y: Number(positionY)
        }
      });
    }
  }, [dispatch, errors.positionX, errors.positionY, positionX, positionY]);

  useEffect(() => {
    const hasValues = !isEmpty(rotation);
    const isValid = !errors.rotation;
    if (hasValues && isValid) {
      dispatch({type: "RotateStageProp", payload: {rotation: Number(rotation)}});
    }
  }, [dispatch, errors.rotation, rotation]);

  return (
    <form>
      <Grid container={true} spacing={2}>
        <Grid item={true} xs={6}>
          <TextField
            variant="outlined"
            label="Position X"
            name="positionX"
            type="number"
            margin="dense"
            InputLabelProps={{shrink: true}}
            disabled={!hasStageProp}
            error={errors.positionX !== undefined}
            inputRef={register({required: true, min: 0, pattern: POSITIVE_INTEGER})}
          />
        </Grid>

        <Grid item={true} xs={6}>
          <TextField
            variant="outlined"
            label="Position Y"
            name="positionY"
            type="number"
            margin="dense"
            InputLabelProps={{shrink: true}}
            disabled={!hasStageProp}
            error={errors.positionY !== undefined}
            inputRef={register({required: true, min: 0, pattern: POSITIVE_INTEGER})}
          />
        </Grid>

        <Grid item={true} xs={6}>
          <TextField
            variant="outlined"
            label="Rotation &deg;"
            name="rotation"
            type="number"
            margin="dense"
            InputLabelProps={{shrink: true}}
            disabled={!hasStageProp}
            error={errors.rotation !== undefined}
            inputRef={register({required: true, min: 0, max: 359, pattern: POSITIVE_INTEGER})}
          />
        </Grid>
      </Grid>
    </form>
  );
};

export default StagePropDetails;
