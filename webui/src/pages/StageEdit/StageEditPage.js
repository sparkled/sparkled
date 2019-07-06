import { withStyles } from '@material-ui/core';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import { ActionCreators } from 'redux-undo';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { setCurrentPage } from '../actions';
import { fetchStage, saveStage } from './actions';
import StageCanvas from './components/StageCanvas';
import './StageEditPage.css';

const { undo, redo, clearHistory } = ActionCreators;

const styles = theme => ({
  container: {
    overflow: 'hidden',
  }
});

class StageEditPage extends Component {

  render() {
    const pageBody = (
      <div className={`d-flex w-100 h-100 ${this.props.classes.container}`}>
        {this.renderContent()}
      </div>
    );

    return <PageContainer body={pageBody} spacing={0} navbar={this.renderNavbar()}/>;
  }

  componentWillReceiveProps(nextProps) {
    const didSave = this.props.saving && !nextProps.saving;
    if (didSave) {
      const saveError = nextProps.saveError;
      if (saveError) {
        Alert.error(`Save failed: ${saveError}`);
      } else {
        Alert.success('Stage saved successfully');
      }
    }
  }

  componentDidMount() {
    const { stageId } = this.props.match.params;
    this.props.setCurrentPage({ pageTitle: 'Edit Stage', pageClass: 'StageEditPage' });
    this.props.fetchStage(stageId);
  }

  renderNavbar() {
    const { canUndo, canRedo, undo, redo, stage, saving } = this.props;

    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={(!saving && stage && canUndo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => undo()}>Undo</span>
        </NavItem>
        <NavItem className={(!saving && stage && canRedo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => redo()}>Redo</span>
        </NavItem>
        <NavItem className={!saving && stage ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => this.props.saveStage(stage)}>Save</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetchError, fetching, stage } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError();
    } else if (stage) {
      return this.renderEditor();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError() {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>Failed to load stage: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderEditor() {
    return (
      <StageCanvas stage={this.props.stage} editable={true}/>
    );
  }

  componentWillUnmount() {
    this.props.clearHistory();
  }
}

function mapStateToProps({ page }) {
  const { past, present, future } = page.stageEdit;

  return {
    fetching: present.fetching,
    fetchError: present.fetchError,
    saving: present.saving,
    saveError: present.saveError,
    stage: present.stage,
    canUndo: past.length > 1,
    canRedo: future.length > 0
  };
}

StageEditPage = connect(mapStateToProps, {
  setCurrentPage,
  fetchStage,
  saveStage,
  undo,
  redo,
  clearHistory
})(StageEditPage);

export default withStyles(styles, { withTheme: true })(StageEditPage);
