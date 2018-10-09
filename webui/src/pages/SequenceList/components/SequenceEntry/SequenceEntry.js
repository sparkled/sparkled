import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { showDeleteModal } from '../../actions';
import './SequenceEntry.css';

class SequenceEntry extends Component {

  state = { dropdownOpen: false };

  render() {
    const { sequence } = this.props;

    return (
      <div className="sequence-entry card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h4 className="mb-0">{sequence.name}</h4>

          <div className="btn-group">
            <Link className="btn btn-secondary" to={`/sequences/${sequence.id}`}>Edit</Link>

            <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle.bind(this)}>
              <DropdownToggle caret/>
              <DropdownMenu right>
                <DropdownItem onClick={this.showDeleteModal.bind(this)}>Delete Sequence</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </div>

        <div className="card-body">
          <div className="d-flex justify-content-between">
            <h6>Album: {sequence.album}</h6>
            <h6 className="ml-3">{this.getFormattedDuration(sequence)}</h6>
          </div>

          <div className="d-flex justify-content-between">
            <h6>Artist: {sequence.artist}</h6>
            <div className="ml-3">
              <span className="badge badge-primary">{sequence.status}</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ dropdownOpen: !prevState.dropdownOpen }));
  }

  showDeleteModal() {
    const { showDeleteModal, sequence } = this.props;
    showDeleteModal(sequence);
  }

  getFormattedDuration(sequence) {
    const date = new Date(null);
    date.setSeconds(Math.floor(sequence.durationFrames / sequence.framesPerSecond));

    // 1970-01-01T00:01:23.000Z
    //               ^^^^^
    return date.toISOString().substr(14, 5);
  }
}

function mapStateToProps({ page: { sequenceList } }) {
  return {
    sequenceToDelete: sequenceList.sequenceToDelete,
    deleting: sequenceList.deleting
  };
}

export default connect(mapStateToProps, { showDeleteModal })(SequenceEntry);
