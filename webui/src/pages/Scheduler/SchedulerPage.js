import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import moment from 'moment';
import FormattedDateTime from '../../components/FormattedDateTime';
import FormattedTime from '../../components/FormattedTime';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchScheduledSequences } from './actions';

class SchedulerPage extends Component {

  state = { startDate: moment() };

  componentDidMount() {
    this.props.fetchScheduledSequences(new Date());
  }

  componentWillReceiveProps(nextProps) {
    const { props } = this;
    if (props.deleting && !nextProps.deleting && !nextProps.deleteError) {
      Alert.success('Sequence unscheduled successfully');
      nextProps.fetchScheduledSequences();
    } else if (props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Sequence scheduled successfully');
      nextProps.fetchScheduledSequences();
    }
  }

  render() {
    const pageBody = (
      <div className="container">
        <div className="row">
          <div className="col-lg-12">
            <DatePicker
              selected={this.state.startDate}
              onChange={this.updateDate.bind(this)}
            />
          </div>
        </div>

        <div className="row">
          <div className="col-lg-12">{this.renderContent()}</div>
        </div>
      </div>
    );

    return <PageContainer body={pageBody}/>;
  }

  renderContent() {
    const { fetchError, fetching } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError();
    } else {
      return this.renderTable();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError() {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>Failed to load sequences: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderTable() {
    return (
      <table className="table table-striped table-bordered">
        <thead className="thead-inverse">
          <tr>
            <th className="text-white text-right">Start Time</th>
            <th className="text-white text-right">End Time</th>
            <th className="text-white">Sequence</th>
            <th className="text-white">Artist</th>
            <th className="text-white">Album</th>
            <th className="text-white text-right">Duration</th>
            <th/>
          </tr>
        </thead>
        <tbody>
          {this.renderRows()}
        </tbody>
      </table>
    );
  }

  renderRows() {
    if (this.props.scheduledSequences.length === 0) {
      return (
        <tr>
          <td colSpan="6">No sequences are scheduled for this date.</td>
        </tr>
      );
    }

    return _.map(this.props.scheduledSequences, scheduledSequence => (
      <tr key={scheduledSequence.id}>
        <td className="align-middle text-right">
          <FormattedDateTime date={scheduledSequence.startTime}/>
        </td>
        <td className="align-middle text-right">
          <FormattedDateTime date={scheduledSequence.endTime}/>
        </td>
        <td className="align-middle">{scheduledSequence.sequence.name}</td>
        <td className="align-middle">{scheduledSequence.sequence.artist}</td>
        <td className="align-middle">{scheduledSequence.sequence.album}</td>
        <td className="align-middle text-right">
          <FormattedTime millis={scheduledSequence.sequence.durationFrames / scheduledSequence.sequence.framesPerSecond}/>
        </td>
        <td className="text-right">
          <button className="btn btn-danger btn-sm"
                  title="Unschedule"
                  onClick={this.unscheduleSequence.bind(this, scheduledSequence)}>
            Unschedule
          </button>
        </td>
      </tr>
    ));
  }

  updateDate(startDate) {
    this.setState({startDate});
    this.props.fetchScheduledSequences(startDate.toDate());
  }

  unscheduleSequence(scheduledSequence) {
    alert('Unscheduling ' + scheduledSequence.startTime);
  }
}

function mapStateToProps({ page: { scheduler } }) {
  return {
    scheduledSequences: scheduler.scheduledSequences,
    fetching: scheduler.fetching,
    fetchError: scheduler.fetchError,
    adding: scheduler.adding,
    addError: scheduler.addError,
    deleting: scheduler.deleting,
    deleteError: scheduler.deleteError
  };
}

export default connect(mapStateToProps, { fetchScheduledSequences: fetchScheduledSequences })(SchedulerPage);
