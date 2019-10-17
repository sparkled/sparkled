import {Card, Theme, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/styles";
import React from "react";

const useStyles = makeStyles((theme: Theme) => ({
  simpleCard: {
    margin: "auto",
    padding: theme.spacing(2)
  }
}));

interface Props extends React.HTMLAttributes<HTMLElement> {}

const SimpleTextCard: React.FC<Props> = props => {
  const classes = useStyles();

  return (
    <Card className={classes.simpleCard}>
      <Typography variant="body2">{props.children}</Typography>
    </Card>
  );
};

export default SimpleTextCard;
