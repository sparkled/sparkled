import {Button, Card, CardActions, CardContent, Theme, Typography} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/styles";
import React from "react";
import {Link} from "react-router-dom";

declare interface Props {
  title: string;
  body: string;
  linkUrl: string;
  linkText: string;
}

const useStyles = makeStyles((theme: Theme) => ({
  error: {
    margin: theme.spacing(3),
    padding: theme.spacing(2)
  }
}));

const ErrorCard: React.FC<Props> = (props: Props) => {
  const classes = useStyles();
  return (
    <Grid item={true} xs={12} sm={6} md={4}>
      <Card className={classes.error}>
        <CardContent>
          <Typography gutterBottom={true} variant="h5" component="h2">{props.title}</Typography>
          <Typography variant="body2" color="textSecondary" component="p">{props.body}</Typography>
        </CardContent>
        <CardActions>
          <Button component={Link} to={props.linkUrl} size="small" color="primary">{props.linkText}</Button>
        </CardActions>
      </Card>
    </Grid>
  );
};

export default ErrorCard;
