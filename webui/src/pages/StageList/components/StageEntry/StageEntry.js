import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { showDeleteModal } from '../../actions';
import './StageEntry.css';

class StageEntry extends Component {

  state = { dropdownOpen: false };

  render() {
    const { stage } = this.props;

    return (
      <div className="stage-entry card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h4 className="mb-0">{stage.name}</h4>

          <div className="btn-group">
            <Link className="btn btn-secondary" to={`/stages/${stage.id}`}>Edit</Link>

            <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle.bind(this)}>
              <DropdownToggle caret/>
              <DropdownMenu right>
                <DropdownItem onClick={this.showDeleteModal.bind(this)}>Delete Stage</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </div>

        <div className="card-body">
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ dropdownOpen: !prevState.dropdownOpen }));
  }

  showDeleteModal() {
    const { showDeleteModal, stage } = this.props;
    showDeleteModal(stage);
  }
}

function mapStateToProps({ page: { stageList } }) {
  return {
    stageToDelete: stageList.stageToDelete,
    deleting: stageList.deleting
  };
}

export default connect(mapStateToProps, { showDeleteModal })(StageEntry);
