import { Card, CardContent, Grid, withStyles } from '@material-ui/core';
import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import PageContainer from '../../components/PageContainer';
import SearchBar from '../../components/SearchBar';
import { setCurrentPage } from '../actions';
import { fetchStages, showAddModal } from './actions';
import AddStageModal from './components/AddStageModal';
import DeleteStageModal from './components/DeleteStageModal';
import StageCard from './components/StageCard';

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  emptyCard: {
    margin: 'auto'
  }
});


class StageListPage extends Component {

  state = { searchQuery: '' };

  constructor(props) {
    super(props);
    this.stageMatchesSearch = this.stageMatchesSearch.bind(this);
  }

  componentDidMount() {
    this.props.setCurrentPage({ pageTitle: 'Stages', pageClass: 'StageListPage' });
    this.props.fetchStages();
  }

  componentWillReceiveProps(nextProps) {
    const { props } = this;
    if (props.deleting && !nextProps.deleting && !nextProps.deleteError) {
      Alert.success('Stage deleted successfully');
      nextProps.fetchStages();
    } else if (props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Stage added successfully');
      nextProps.fetchStages();
    }
  }

  render() {
    const { classes, fetching, showAddModal } = this.props;

    const pageBody = (
      <div className={classes.root}>
        <SearchBar placeholderText="Search stages" fetching={fetching} onAddButtonClick={showAddModal}
                   onSearch={this.filterStages}
        />

        <Grid container spacing={3}>
          {this.renderContent()}
        </Grid>

        <AddStageModal/>
        <DeleteStageModal/>
      </div>
    );

    return <PageContainer body={pageBody}/>;
  }

  renderContent() {
    const { fetching, fetchError } = this.props;

    if (fetching) {
      return [];
    } else if (fetchError) {
      return this.renderError(`Failed to load stages: ${fetchError}`);
    } else {
      const filteredStages = this.getFilteredStages();
      if (_.isEmpty(filteredStages)) {
        return this.renderEmpty();
      } else {
        return this.renderStages(filteredStages);
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
          No stages found.
        </CardContent>
      </Card>
    );
  }

  renderStages(stages) {
    return _.map(stages, stage => (
      <Grid item key={stage.id} xs={12} sm={6} md={4}>
        <StageCard stage={stage}/>
      </Grid>
    ));
  }

  filterStages = searchQuery => this.setState({ searchQuery });

  getFilteredStages = () => _.filter(this.props.stages || [], this.stageMatchesSearch);

  stageMatchesSearch = stage => {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();
    return !searchQuery || _.includes(stage.name.toLowerCase(), searchQuery);
  }
}

function mapStateToProps({ page: { stageList } }) {
  return {
    stages: stageList.stages,
    fetching: stageList.fetching,
    fetchError: stageList.fetchError,
    adding: stageList.adding,
    addError: stageList.addError,
    deleting: stageList.deleting,
    deleteError: stageList.deleteError
  };
}

StageListPage = connect(mapStateToProps, { setCurrentPage, showAddModal, fetchStages })(StageListPage);
export default withStyles(styles)(StageListPage);
