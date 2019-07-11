import { Card, CardContent, CardHeader, IconButton, Menu, MenuItem, Typography } from '@material-ui/core';
import { MoreVert } from '@material-ui/icons';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { getFormattedDuration } from '../../../../utils/dateUtils';
import { showDeleteModal } from '../../actions';

class SongCard extends Component {

  state = { anchorEl: null };

  render() {
    const { song } = this.props;
    const { anchorEl } = this.state;

    return (
      <>
        <Card>
          <CardHeader
            action={
              <IconButton onClick={this.openMenu}>
                <MoreVert/>
              </IconButton>
            }
            title={song.name}
            subheader={`${song.artist} (${song.album})`}
          />

          <CardContent>
            <Typography>
              Duration: {getFormattedDuration(Math.floor(song.durationMs / 1000))}
            </Typography>
          </CardContent>
        </Card>

        <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={this.closeMenu}>
          <MenuItem onClick={this.showDeleteModal}>Delete song</MenuItem>
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

    const { showDeleteModal, song } = this.props;
    showDeleteModal(song);
  }
}

function mapStateToProps({ page: { songList } }) {
  return {
    songToDelete: songList.songToDelete,
    deleting: songList.deleting
  };
}

export default connect(mapStateToProps, { showDeleteModal })(SongCard);
