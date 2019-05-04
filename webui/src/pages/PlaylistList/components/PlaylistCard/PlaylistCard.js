import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
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
import { playPlaylist, showDeleteModal } from '../../actions';

const styles = theme => ({
  topRow: {
    marginBottom: 8
  },
});

class PlaylistCard extends Component {

  state = { anchorEl: null };

  render() {
    const { classes, playlist } = this.props;
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
            title={playlist.name}
          />

          <CardContent>
            <Grid container justify="space-between" className={classes.topRow}>
              <Grid item>
                <Typography>Sequences: {playlist.sequenceCount}</Typography>
              </Grid>
              <Grid item>
                <Typography>Duration: {getFormattedDuration(Math.floor(playlist.durationSeconds))}</Typography>
              </Grid>
            </Grid>
          </CardContent>
        </Card>

        <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={this.closeMenu}>
          <MenuItem onClick={this.playPlaylist}>Play playlist</MenuItem>
          <MenuItem component={Link} to={`/playlists/${playlist.id}`}>Edit playlist</MenuItem>
          <MenuItem onClick={this.showDeleteModal}>Delete playlist</MenuItem>
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

   playPlaylist = () => {
    const { playPlaylist, playlist } = this.props;
    playPlaylist(playlist.id);
  }

  showDeleteModal = () => {
    this.closeMenu();

    const { showDeleteModal, playlist } = this.props;
    showDeleteModal(playlist);
  }
}

function mapStateToProps({ page: { playlistList } }) {
  return {
    playlistToDelete: playlistList.playlistToDelete,
    deleting: playlistList.deleting
  };
}

PlaylistCard = withStyles(styles)(PlaylistCard);
export default connect(mapStateToProps, { showDeleteModal, playPlaylist })(PlaylistCard);
