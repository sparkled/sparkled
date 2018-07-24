import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { fetchStages } from '../../services/stage/actions';
import { showAddModal } from './actions';
import AddStageModal from './components/AddStageModal';
import DeleteStageModal from './components/DeleteStageModal';
import StageEntry from './components/StageEntry';

class StageListPage extends Component {

  state = { searchQuery: '' };

  componentDidMount() {
    this.props.fetchStages();
  }

  componentWillReceiveProps(nextProps) {
    if (!this.props.deleteSuccess && nextProps.deleteSuccess) {
      Alert.success('Stage deleted successfully');
      this.props.fetchStages();
    } else if (!this.props.addSuccess && nextProps.addSuccess) {
      Alert.success('Stage added successfully');
      this.props.fetchStages();
    }
  }

  render() {
    const pageBody = (
      <div>
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
      <div className="card card-outline-danger">
        <div className="card-block">
          <p>Failed to load stages: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderStages() {
    if (_.isEmpty(this.props.stages)) {
      return (
        <div className="card card-outline-info">
          <div className="card-block">
            No stages have been added.
          </div>
        </div>
      );
    }

    const stages = _(this.props.stages)
      .filter(this.stageMatchesSearch.bind(this))
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

    if (!searchQuery) {
      return true;
    }

    const { name } = stage;
    return _.filter([name], field => _.includes(field.toLowerCase(), searchQuery)).length > 0;
  }
}

function mapStateToProps({ data: { stages } }) {
  return {
    stages: stages.data,
    fetching: stages.fetching,
    fetchError: stages.fetchError,
    addSuccess: stages.addSuccess,
    deleteSuccess: stages.deleteSuccess
  };
}

export default connect(mapStateToProps, { showAddModal, fetchStages })(StageListPage);
