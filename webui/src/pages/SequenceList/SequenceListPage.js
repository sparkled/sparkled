import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import { setCurrentPage } from '../actions';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchSequences, showAddModal } from './actions';
import { fetchSongs } from '../SongList/actions';
import { fetchStages } from '../StageList/actions';
import AddSequenceModal from './components/AddSequenceModal';
import DeleteSequenceModal from './components/DeleteSequenceModal';
import SequenceEntry from './components/SequenceEntry';

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
    } else if (props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Sequence added successfully');
      nextProps.fetchSequences();
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

        <AddSequenceModal songs={this.props.songs} stages={this.props.stages}/>
        <DeleteSequenceModal/>
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
  }

  renderNavbar() {
    const canAdd = this.props.sequences && this.props.songs && this.props.stages;
    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={canAdd ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.props.showAddModal}>Add Sequence</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetching, fetchingSongs, fetchingStages, fetchError, fetchSongsError, fetchStagesError } = this.props;

    if (fetching || fetchingSongs || fetchingStages) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError(`Failed to load sequences: ${fetchError}`);
    } else if (fetchSongsError) {
      return this.renderError(`Failed to load songs: ${fetchSongsError}`);
    } else if (fetchStagesError) {
      return this.renderError(`Failed to load stages: ${fetchStagesError}`);
    } else {
      return this.renderSequences();
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

  renderSequences() {
    if (_.isEmpty(this.props.sequences)) {
      return (
        <div className="card border-info">
          <div className="card-body">
            No sequences have been added.
          </div>
        </div>
      );
    }

    const sequences = _(this.props.sequences)
      .filter(this.sequenceMatchesSearch)
      .map(sequence => (
        <div key={sequence.id} className="col-md-6 col-lg-4 mb-4">
          <SequenceEntry sequence={sequence}/>
        </div>
      ))
      .value();

    return <div className="row">{sequences}</div>;
  }

  sequenceMatchesSearch(sequence) {
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

export default connect(mapStateToProps, {
  setCurrentPage, showAddModal, fetchSequences, fetchSongs, fetchStages
})(SequenceListPage);
