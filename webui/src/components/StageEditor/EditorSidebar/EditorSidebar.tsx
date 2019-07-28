import {ExpansionPanel, ExpansionPanelDetails, ExpansionPanelSummary, Theme, Typography} from "@material-ui/core";
import {ExpandMore} from "@material-ui/icons";
import {makeStyles} from "@material-ui/styles";
import React, {HTMLAttributes} from "react";
import StagePropDetails from "./StagePropDetails";

const useStyles = makeStyles((theme: Theme) => ({
  heading: {
    fontSize: theme.typography.pxToRem(15)
  },
}));

const EditorSidebar: React.FC<HTMLAttributes<void>> = props => {
  const classes = useStyles();

  return (
    <div className={props.className || ""}>
      <ExpansionPanel defaultExpanded={true}>
        <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
          <Typography className={classes.heading}>Stage Prop Details</Typography>
        </ExpansionPanelSummary>
        <ExpansionPanelDetails>
          <StagePropDetails/>
        </ExpansionPanelDetails>
      </ExpansionPanel>
    </div>
  );
};

export default EditorSidebar;
