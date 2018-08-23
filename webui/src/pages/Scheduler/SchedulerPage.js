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
import { fetchScheduledSongs } from './actions';

class SchedulerPage extends Component {

  state = { startDate: moment() };

  componentDidMount() {
    this.props.fetchScheduledSongs(new Date());
  }

  componentWillReceiveProps(nextProps) {
    const { props } = this;
    if (props.deleting && !nextProps.deleting && !nextProps.deleteError) {
      Alert.success('Song unscheduled successfully');
      nextProps.fetchScheduledSongs();
    } else if (props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Song scheduled successfully');
      nextProps.fetchScheduledSongs();
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
          <p>Failed to load songs: {this.props.fetchError}</p>
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
            <th className="text-white">Song</th>
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
    if (this.props.scheduledSongs.length === 0) {
      return (
        <tr>
          <td colSpan="6">No songs are scheduled for this date.</td>
        </tr>
      );
    }

    return _.map(this.props.scheduledSongs, scheduledSong => (
      <tr key={scheduledSong.id}>
        <td className="align-middle text-right">
          <FormattedDateTime date={scheduledSong.startTime}/>
        </td>
        <td className="align-middle text-right">
          <FormattedDateTime date={scheduledSong.endTime}/>
        </td>
        <td className="align-middle">{scheduledSong.song.name}</td>
        <td className="align-middle">{scheduledSong.song.artist}</td>
        <td className="align-middle">{scheduledSong.song.album}</td>
        <td className="align-middle text-right">
          <FormattedTime millis={scheduledSong.song.durationFrames / scheduledSong.song.framesPerSecond}/>
        </td>
        <td className="text-right">
          <button className="btn btn-danger btn-sm"
                  title="Unschedule"
                  onClick={this.unscheduleSong.bind(this, scheduledSong)}>
            Unschedule
          </button>
        </td>
      </tr>
    ));
  }

  updateDate(startDate) {
    this.setState({startDate});
    this.props.fetchScheduledSongs(startDate.toDate());
  }

  unscheduleSong(scheduledSong) {
    alert('Unscheduling ' + scheduledSong.startTime);
  }
}

function mapStateToProps({ page: { scheduler } }) {
  return {
    scheduledSongs: scheduler.scheduledSongs,
    fetching: scheduler.fetching,
    fetchError: scheduler.fetchError,
    adding: scheduler.adding,
    addError: scheduler.addError,
    deleting: scheduler.deleting,
    deleteError: scheduler.deleteError
  };
}

export default connect(mapStateToProps, { fetchScheduledSongs })(SchedulerPage);
