import {Button, Grid} from "@material-ui/core";
import React, {useContext} from "react";
import {StageEditorDispatchContext} from "../StageEditorReducer";
import stagePropTypes, {StagePropType} from "../../../pages/StageEdit/stagePropTypes";
import {values} from "lodash";

const AddStageProp: React.FC = () => {
  const dispatch = useContext(StageEditorDispatchContext);

  const addStageProp = (type: StagePropType) => {
    dispatch({type: "AddStageProp", payload: {type}});
  };

  const buttons = values(stagePropTypes).map(type => (
    <Grid key={type.id} item xs={4}>
      <Button onClick={() => addStageProp(type)}>{type.name}</Button>
    </Grid>
  ));

  return (
    <Grid container spacing={2}>
      {buttons}
    </Grid>
  );
};

export default AddStageProp;
