import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
import IconButton from '@material-ui/core/IconButton';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Typography from '@material-ui/core/Typography';
import MoreVertIcon from '@material-ui/icons/MoreVert';
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
                <MoreVertIcon/>
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
