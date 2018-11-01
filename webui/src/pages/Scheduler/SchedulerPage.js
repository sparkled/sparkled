import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import { setCurrentPage } from '../actions';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchScheduledJobs, showAddModal } from './actions';
import { fetchPlaylists } from '../PlaylistList/actions';
import AddScheduledJobModal from './components/AddScheduledJobModal';
import DeleteScheduledJobModal from './components/DeleteScheduledJobModal';
import ScheduledJobEntry from './components/ScheduledJobEntry';

class SchedulerPage extends Component {

  state = { searchQuery: '' };

  constructor(props) {
    super(props);
    this.scheduledJobMatchesSearch = this.scheduledJobMatchesSearch.bind(this);
  }

  componentDidMount() {
    this.props.setCurrentPage({ pageTitle: 'Scheduled Jobs', pageClass: 'SchedulerPage' });
    this.props.fetchScheduledJobs();
    this.props.fetchPlaylists();
  }

  componentWillReceiveProps(nextProps) {
    const { props } = this;
    if (props.deleting && !nextProps.deleting && !nextProps.deleteError) {
      Alert.success('Scheduled job deleted successfully');
      nextProps.fetchScheduledJobs();
    } else if (props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Scheduled job added successfully');
      nextProps.fetchScheduledJobs();
    }
  }

  render() {
    const pageBody = (
      <div className="container">
        <div className="row">
          <div className="col-lg-12 input-group input-group-lg my-4">
            <input type="text" className="form-control" placeholder="Search..." value={this.state.searchQuery}
                   onChange={e => this.setState({ searchQuery: e.target.value })}/>
          </div>
        </div>

        <div className="row">
          <div className="col-lg-12">{this.renderContent()}</div>
        </div>

        <AddScheduledJobModal playlists={this.props.playlists}/>
        <DeleteScheduledJobModal/>
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
  }

  renderNavbar() {
    const canAdd = this.props.scheduledJobs;
    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={canAdd ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.props.showAddModal}>Add Scheduled Job</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetching, fetchingPlaylists, fetchError, fetchPlaylistsError } = this.props;

    if (fetching || fetchingPlaylists) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError(`Failed to load scheduled jobs: ${fetchError}`);
    } else if (fetchPlaylistsError) {
      return this.renderError(`Failed to load playlists: ${fetchPlaylistsError}`);
    } else {
      return this.renderScheduledJobs();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError(error) {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>{error}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderScheduledJobs() {
    if (_.isEmpty(this.props.scheduledJobs)) {
      return (
        <div className="card border-info">
          <div className="card-body">
            No scheduled jobs have been added.
          </div>
        </div>
      );
    }

    const scheduledJobs = _(this.props.scheduledJobs)
      .filter(this.scheduledJobMatchesSearch)
      .map(scheduledJob => (
        <div key={scheduledJob.id} className="col-md-6 col-lg-4 mb-4">
          <ScheduledJobEntry scheduledJob={scheduledJob}/>
        </div>
      ))
      .value();

    return <div className="row">{scheduledJobs}</div>;
  }

  scheduledJobMatchesSearch(scheduledJob) {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();
    return !searchQuery || _.includes(scheduledJob.playlistName.toLowerCase(), searchQuery);
  }
}

function mapStateToProps({ page: { scheduler, playlistList } }) {
  return {
    scheduledJobs: scheduler.scheduledJobs,
    fetching: scheduler.fetching,
    fetchError: scheduler.fetchError,
    playlists: playlistList.playlists,
    fetchingPlaylists: playlistList.fetching,
    fetchPlaylistsError: playlistList.fetchError,
    adding: scheduler.adding,
    addError: scheduler.addError,
    deleting: scheduler.deleting,
    deleteError: scheduler.deleteError
  };
}

export default connect(mapStateToProps, { setCurrentPage, showAddModal, fetchScheduledJobs, fetchPlaylists })(SchedulerPage);
