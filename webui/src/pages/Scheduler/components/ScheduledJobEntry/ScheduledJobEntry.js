import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { showDeleteModal } from '../../actions';
import './ScheduledJobEntry.css';

class ScheduledJobEntry extends Component {

  state = { dropdownOpen: false };

  constructor(props) {
    super(props);
    this.toggle = this.toggle.bind(this);
    this.showDeleteModal = this.showDeleteModal.bind(this);
  }

  render() {
    const { scheduledJob } = this.props;

    return (
      <div className="PlaylistEntry card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h4 className="mb-0">{scheduledJob.action}</h4>

          <div className="btn-group">
            <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
              <DropdownToggle caret/>
              <DropdownMenu right>
                <DropdownItem onClick={this.showDeleteModal}>Delete scheduled job</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </div>

        <div className="card-body">
          <div className="d-flex justify-content-between">
            <h6>Expression: {scheduledJob.cronExpression}</h6>
          </div>
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ dropdownOpen: !prevState.dropdownOpen }));
  }

  showDeleteModal() {
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

export default connect(mapStateToProps, { showDeleteModal })(ScheduledJobEntry);
