import { Card, CardContent, CardHeader, Grid, IconButton, Menu, MenuItem, Typography } from '@material-ui/core';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { getFormattedDuration } from '../../../../utils/dateUtils';
import { playPlaylist, showDeleteModal } from '../../actions';

class PlaylistCard extends Component {

  state = { anchorEl: null };

  render() {
    const { playlist } = this.props;
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
            <Grid container justify="space-between">
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

export default connect(mapStateToProps, { showDeleteModal, playPlaylist })(PlaylistCard);
