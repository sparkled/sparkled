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
import { showDeleteModal } from '../../actions';

class StageCard extends Component {

  state = { anchorEl: null };

  render() {
    const { scheduledJob } = this.props;
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
            title={scheduledJob.action}
          />

          <CardContent>
            <Typography>
              Expression: {scheduledJob.cronExpression}
            </Typography>
          </CardContent>
        </Card>

        <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={this.closeMenu}>
          <MenuItem onClick={this.showDeleteModal}>Delete scheduled job</MenuItem>
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

    const { showDeleteModal, scheduledJob } = this.props;
    showDeleteModal(scheduledJob);
  }
}

function mapStateToProps({ page: { scheduler } }) {
  return {
    scheduledJobToDelete: scheduler.scheduledJobToDelete,
    deleting: scheduler.deleting
  };
}

export default connect(mapStateToProps, { showDeleteModal })(StageCard);
