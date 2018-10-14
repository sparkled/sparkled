import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { setCurrentPage } from '../actions';
import { Nav, NavItem } from 'reactstrap';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchStages, showAddModal } from './actions';
import AddStageModal from './components/AddStageModal';
import DeleteStageModal from './components/DeleteStageModal';
import StageEntry from './components/StageEntry';

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
    } else if (this.props.adding && !nextProps.adding && !nextProps.addError) {
      Alert.success('Stage added successfully');
      nextProps.fetchStages();
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

        <AddStageModal/>
        <DeleteStageModal/>
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
  }

  renderNavbar() {
    return (
      <Nav className="ml-auto" navbar>
        <NavItem>
          <span className="nav-link" onClick={this.props.showAddModal}>Add Stage</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetchError, fetching } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError();
    } else {
      return this.renderStages();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError() {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>Failed to load stages: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderStages() {
    if (_.isEmpty(this.props.stages)) {
      return (
        <div className="card border-info">
          <div className="card-body">
            No stages have been added.
          </div>
        </div>
      );
    }

    const stages = _(this.props.stages)
      .filter(this.stageMatchesSearch)
      .map(stage => (
        <div key={stage.id} className="col-md-6 col-lg-4 mb-4">
          <StageEntry stage={stage}/>
        </div>
      ))
      .value();

    return <div className="row">{stages}</div>;
  }

  stageMatchesSearch(stage) {
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

export default connect(mapStateToProps, { setCurrentPage, showAddModal, fetchStages })(StageListPage);
