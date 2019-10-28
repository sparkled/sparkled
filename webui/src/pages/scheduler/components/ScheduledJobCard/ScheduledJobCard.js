import { Card, CardContent, CardHeader, IconButton, Menu, MenuItem, Typography } from '@material-ui/core';
import { MoreVert } from '@material-ui/icons';
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
                <MoreVert/>
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
