import { withStyles } from '@material-ui/core';
import Card from '@material-ui/core/es/Card/Card';
import CardContent from '@material-ui/core/es/CardContent/CardContent';
import Grid from '@material-ui/core/Grid/Grid';
import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import PageContainer from '../../components/PageContainer';
import SearchBar from '../../components/SearchBar';
import { setCurrentPage } from '../actions';
import { fetchScheduledJobs, showAddModal } from './actions';
import { fetchPlaylists } from '../PlaylistList/actions';
import AddScheduledJobModal from './components/AddScheduledJobModal';
import DeleteScheduledJobModal from './components/DeleteScheduledJobModal';
import ScheduledJobCard from './components/ScheduledJobCard';

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  emptyCard: {
    margin: 'auto'
  }
});

class SchedulerPage extends Component {

  state = { searchQuery: '' };

  componentDidMount() {
    this.props.setCurrentPage({ pageTitle: 'Scheduler', pageClass: 'SchedulerPage' });
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
    const { classes, fetching, showAddModal } = this.props;

    const pageBody = (
      <div className={classes.root}>
        <SearchBar placeholderText="Search scheduled jobs" fetching={fetching} onAddButtonClick={showAddModal}
                   onSearch={this.filterScheduledJobs}
        />

        <Grid container spacing={24}>
          {this.renderContent()}
        </Grid>

        <AddScheduledJobModal playlists={this.props.playlists}/>
        <DeleteScheduledJobModal/>
      </div>
    );

    return <PageContainer body={pageBody}/>;
  }

  renderContent() {
    const { fetching, fetchingPlaylists, fetchError, fetchPlaylistsError } = this.props;

    if (fetching || fetchingPlaylists) {
      return [];
    } else if (fetchError) {
      return this.renderError(`Failed to load scheduled jobs: ${fetchError}`);
    } else if (fetchPlaylistsError) {
      return this.renderError(`Failed to load playlists: ${fetchPlaylistsError}`);
    } else {
      const filteredScheduledJobs = this.getFilteredScheduledJobs();
      if (_.isEmpty(filteredScheduledJobs)) {
        return this.renderEmpty();
      } else {
        return this.renderScheduledJobs(filteredScheduledJobs);
      }
    }
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

  renderEmpty() {
    return (
      <Card className={this.props.classes.emptyCard}>
        <CardContent>
          No scheduled jobs found.
        </CardContent>
      </Card>
    );
  }

  renderScheduledJobs(scheduledJobs) {
    return _.map(scheduledJobs, scheduledJob => (
      <Grid item key={scheduledJob.id} xs={12} sm={6} md={4}>
        <ScheduledJobCard scheduledJob={scheduledJob}/>
      </Grid>
    ));
  }

  filterScheduledJobs = searchQuery => this.setState({ searchQuery });

  getFilteredScheduledJobs = () => _.filter(this.props.scheduledJobs || [], this.scheduledJobMatchesSearch);

  scheduledJobMatchesSearch = scheduledJob => {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();
    return !searchQuery || _.includes(scheduledJob.action.toLowerCase(), searchQuery);
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

SchedulerPage = withStyles(styles)(SchedulerPage);
export default connect(mapStateToProps, { setCurrentPage, showAddModal, fetchScheduledJobs, fetchPlaylists })(SchedulerPage);
