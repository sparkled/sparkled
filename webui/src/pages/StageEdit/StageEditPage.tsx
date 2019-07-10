import {IconButton, Snackbar, Theme} from "@material-ui/core";
import SaveIcon from "@material-ui/icons/Save";
import {makeStyles} from "@material-ui/styles";
import React, {useEffect, useReducer, useState} from "react";
import {RouteComponentProps} from "react-router-dom";
import ErrorCard from "../../components/ErrorCard";
import PageContainer from "../../components/PageContainer";
import {loadStage, saveStage} from "../../rest/StageRestService";
import {StageViewModel} from "../../types/ViewModel";
import {setPageTitle} from "../../utils/pageUtils";
import StageCanvas from "./components/StageCanvas";

const useStyles = makeStyles((theme: Theme) => ({
  container: {
    display: "flex",
    justifyContent: "center",
    width: "100%",
    height: "100%",
    overflow: "hidden"
  }
}));

class State {
  public mounted: boolean = false;
  public stage: StageViewModel | null = null;
  public loading: boolean = false;
  public loadError: [string, string] | null = null;
  public saving: boolean = false;
  public saveError: [string, string] | null = null;
}

type Action =
  | { type: "Load"; }
  | { type: "LoadSuccess"; payload: StageViewModel }
  | { type: "LoadFailure"; payload: [string, string] | null }
  | { type: "Save"; }
  | { type: "SaveSuccess"; }
  | { type: "SaveFailure"; payload: [string, string] | null };

const reducer: React.Reducer<State, Action> = (state, action): State => {
  switch (action.type) {
    case "Load":
      return {...state, mounted: true, loading: true, loadError: null};
    case "LoadSuccess":
      return {...state, loading: false, stage: action.payload};
    case "LoadFailure":
      return {...state, loading: false, loadError: action.payload};
    case "Save":
      return {...state, mounted: true, saving: true, saveError: null};
    case "SaveSuccess":
      return {...state, saving: false};
    case "SaveFailure":
      return {...state, saving: false, saveError: action.payload};
    default:
      return state;
  }
};

type Props = RouteComponentProps<{ stageId: string | undefined }>;

const StageEditPage: React.FC<Props> = props => {
  const [state, dispatch] = useReducer(reducer, new State());
  const [saved, setSaved] = useState(false);
  const classes = useStyles();

  useEffect(() => {
    if (!state.mounted) {
      setPageTitle("Edit Stage");
      dispatch({type: "Load"});
      loadStage(Number(props.match.params.stageId),
        stage => dispatch({type: "LoadSuccess", payload: stage}),
        error => dispatch({type: "LoadFailure", payload: error}),
      );
    }
  });

  let content = <></>;
  if (state.loadError !== null) {
    const [title, body] = state.loadError;
    content = <ErrorCard title={title} body={body} linkUrl="/stages" linkText="Return to stage list"/>;
  } else if (state.stage) {
    content = <StageCanvas stage={state.stage} editable={true}/>;
  }

  const save = () => {
    dispatch({type: "Save"});
    saveStage(state.stage!,
      () => {
        setSaved(true);
        dispatch({type: "SaveSuccess"});
      },
      error => dispatch({type: "SaveFailure", payload: error})
    );
  };

  const pageBody = <div className={classes.container}>{content}</div>;
  const actions = (
    <IconButton onClick={save} disabled={state.stage === null || state.saving}>
      <SaveIcon/>
    </IconButton>
  );

  const closeSnackbar = () => setSaved(false);
  return (
    <>
      <PageContainer body={pageBody} spacing={0} actions={actions}/>
      <Snackbar
        open={saved}
        anchorOrigin={{vertical: "bottom", horizontal: "right"}}
        message="Stage saved"
        autoHideDuration={3000}
        onClose={closeSnackbar}
      />
    </>
  );
};

export default StageEditPage;
