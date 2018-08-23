import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { ActionCreators } from 'redux-undo';
import Alert from 'react-s-alert';
import { Nav, NavItem } from 'reactstrap';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import PropSelector from './components/PropSelector';
import StageCanvas from './components/StageCanvas';
import StagePropList from './components/StagePropList';
import { fetchStage, saveStage } from './actions';
import './StageEditPage.css';

const { undo, redo, clearHistory } = ActionCreators;

class StageEditPage extends Component {

  render() {
    const pageBody = (
      <div className="d-flex w-100 h-100">
        {this.renderContent()}
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()} className="stage-editor"/>;
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
    this.props.fetchStage(stageId);
  }

  renderNavbar() {
    const { canUndo, canRedo, undo, redo, stage } = this.props;

    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={(stage && canUndo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => undo()}>Undo</span>
        </NavItem>
        <NavItem className={(stage && canRedo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => redo()}>Redo</span>
        </NavItem>
        <NavItem className={stage ? '' : 'd-none'}>
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
      <div className="card card-outline-danger">
        <div className="card-body">
          <p>Failed to load stage: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderEditor() {
    return (
      <Fragment key="editor">
        <div className="d-flex flex-column flex-grow-1 h-100">
          <PropSelector className="flex-grow-0"/>
          <div className="stage-canvas-container d-flex flex-grow-1 align-items-center justify-content-center">
            <StageCanvas/>
          </div>
        </div>

        <StagePropList className="flex-shrink-0 h-100"/>
      </Fragment>
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

export default connect(mapStateToProps, { fetchStage, saveStage, undo, redo, clearHistory })(StageEditPage);
