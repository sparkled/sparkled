import { withStyles } from '@material-ui/core';
import { Card, CardContent, Grid } from '@material-ui/core';
import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import PageContainer from '../../components/PageContainer';
import SearchBar from '../../components/SearchBar';
import { setCurrentPage } from '../actions';
import { fetchSongs } from '../SongList/actions';
import { fetchStages } from '../StageList/actions';
import { fetchSequences, showAddModal } from './actions';
import AddSequenceModal from './components/AddSequenceModal';
import DeleteSequenceModal from './components/DeleteSequenceModal';
import SequenceCard from './components/SequenceCard';

const styles = () => ({
  root: {
    flexGrow: 1
  },
  emptyCard: {
    margin: 'auto'
  }
});


class SequenceListPage extends Component {

  state = { searchQuery: '' };

  constructor(props) {
    super(props);
    this.sequenceMatchesSearch = this.sequenceMatchesSearch.bind(this);
  }

  componentDidMount() {
    this.props.setCurrentPage({ pageTitle: 'Sequences', pageClass: 'SequenceListPage' });
    this.props.fetchSequences();
    this.props.fetchSongs();
    this.props.fetchStages();
  }

  componentWillReceiveProps(nextProps) {
    const { props } = this;
    if (props.deleting && !nextProps.deleting && !nextProps.deleteError) {
      Alert.success('Sequence deleted successfully');
      nextProps.fetchSequences();
    } else if (this.props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Sequence added successfully');
      nextProps.fetchSequences();
    }
  }

  render() {
    const { classes, fetching, showAddModal, songs, stages } = this.props;

    const pageBody = (
      <div className={classes.root}>
        <SearchBar placeholderText="Search sequences" fetching={fetching} onAddButtonClick={showAddModal}
                   onSearch={this.filterSequences}
        />

        <Grid container spacing={3}>
          {this.renderContent()}
        </Grid>

        <AddSequenceModal songs={songs} stages={stages}/>
        <DeleteSequenceModal/>
      </div>
    );

    return <PageContainer body={pageBody}/>;
  }

  renderContent() {
    const { fetching, fetchingSongs, fetchingStages, fetchError, fetchSongsError, fetchStagesError } = this.props;

    if (fetching || fetchingSongs || fetchingStages) {
      return [];
    } else if (fetchError) {
      return this.renderError(`Failed to load sequences: ${fetchError}`);
    } else if (fetchSongsError) {
      return this.renderError(`Failed to load songs: ${fetchSongsError}`);
    } else if (fetchStagesError) {
      return this.renderError(`Failed to load stages: ${fetchStagesError}`);
    } else {
      const filteredSequences = this.getFilteredSequences();
      if (_.isEmpty(filteredSequences)) {
        return this.renderEmpty();
      } else {
        return this.renderSequences(filteredSequences);
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
          No sequences found.
        </CardContent>
      </Card>
    );
  }

  renderSequences(sequences) {
    return _.map(sequences, sequence => (
      <Grid item key={sequence.id} xs={12} sm={6} md={4}>
        <SequenceCard sequence={sequence}/>
      </Grid>
    ));
  }

  filterSequences = searchQuery => {
    this.setState({ searchQuery });
  }

  getFilteredSequences = () => {
    return _.filter(this.props.sequences || [], this.sequenceMatchesSearch);
  }

  sequenceMatchesSearch = sequence => {
    const searchQuery = this.state.searchQuery.trim().toLowerCase();
    return !searchQuery || _.includes(sequence.name.toLowerCase(), searchQuery);
  }
}

function mapStateToProps({ page: { sequenceList, songList, stageList } }) {
  return {
    sequences: sequenceList.sequences,
    fetching: sequenceList.fetching,
    fetchError: sequenceList.fetchError,
    stages: stageList.stages,
    fetchingStages: stageList.fetching,
    fetchStagesError: stageList.fetchError,
    songs: songList.songs,
    fetchingSongs: songList.fetching,
    fetchSongsError: songList.fetchError,
    adding: sequenceList.adding,
    addError: sequenceList.addError,
    deleting: sequenceList.deleting,
    deleteError: sequenceList.deleteError
  };
}

SequenceListPage = connect(mapStateToProps, {
  setCurrentPage,
  showAddModal,
  fetchSequences,
  fetchSongs,
  fetchStages
})(SequenceListPage);
export default withStyles(styles)(SequenceListPage);
