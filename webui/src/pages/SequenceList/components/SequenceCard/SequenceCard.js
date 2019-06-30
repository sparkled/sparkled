import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
import blue from '@material-ui/core/colors/blue';
import green from '@material-ui/core/colors/green';
import orange from '@material-ui/core/colors/orange';
import Chip from '@material-ui/core/es/Chip/Chip';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import { withStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { getFormattedDuration } from '../../../../utils/dateUtils';
import { showDeleteModal } from '../../actions';
import * as sequenceStatuses from '../../sequenceStatuses';

const styles = theme => ({
  topRow: {
    marginBottom: 8
  },
  [sequenceStatuses.NEW]: {
    background: blue[500]
  },
  [sequenceStatuses.DRAFT]: {
    background: orange[600]
  },
  [sequenceStatuses.PUBLISHED]: {
    background: green[500]
  }
});

class SequenceCard extends Component {

  state = { anchorEl: null };

  render() {
    const { classes, sequence } = this.props;
    const { anchorEl } = this.state;

    return (
      <>
        <Card>
          <CardHeader
            action={
              <IconButton onClick={this.openMenu}>
                <MoreVertIcon/>
              </IconButton>
            }
            title={sequence.name}
          />

          <CardContent>
            <Grid container justify="space-between" className={classes.topRow}>
              <Grid item>
                <Typography>Song: {sequence.songName}</Typography>
              </Grid>
              <Grid item>
                <Typography>Duration: {getFormattedDuration(Math.floor(sequence.durationSeconds))}</Typography>
              </Grid>
            </Grid>

            <Grid container justify="space-between">
              <Grid item>
                <Typography>Stage: {sequence.stageName}</Typography>
              </Grid>
              <Grid item>
                <Chip className={classes[sequence.status]} label={sequence.status}/>
              </Grid>
            </Grid>
          </CardContent>
        </Card>

        <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={this.closeMenu}>
          <MenuItem component={Link} to={`/sequences/${sequence.id}`}>Edit sequence</MenuItem>
          <MenuItem onClick={this.showDeleteModal}>Delete sequence</MenuItem>
        </Menu>
      </>
    );
  }

  openMenu = event => {
    this.setState({ anchorEl: event.currentTarget });
  }

  closeMenu = () => {
    this.setState({ anchorEl: null });
  }

  showDeleteModal = () => {
    this.closeMenu();

    const { showDeleteModal, sequence } = this.props;
    showDeleteModal(sequence);
  }
}

function mapStateToProps({ page: { sequenceList } }) {
  return {
    sequenceToDelete: sequenceList.sequenceToDelete,
    deleting: sequenceList.deleting
  };
}

SequenceCard = connect(mapStateToProps, { showDeleteModal })(SequenceCard);
export default withStyles(styles)(SequenceCard);
