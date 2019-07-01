import React, { Component } from 'react';
import { connect } from 'react-redux';
import { ActionCreators } from 'redux-undo';
import Alert from 'react-s-alert';
import SplitPane from 'react-split-pane';
import { Nav, NavItem } from 'reactstrap';
import { setCurrentPage }  from '../actions';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import PropSelector from './components/PropSelector';
import StageCanvasV2 from './components/StageCanvasV2';
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

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
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
      <SplitPane split="vertical" defaultSize={300} primary="second" allowResize={false}>
        <SplitPane split="horizontal" defaultSize={80} allowResize={false} pane2ClassName="stage-canvas-container">
          <PropSelector/>
          <StageCanvasV2 stage={this.props.stage} editable={true}/>
        </SplitPane>

        <StagePropList className="flex-shrink-0 h-100"/>
      </SplitPane>
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

export default connect(mapStateToProps, { setCurrentPage, fetchStage, saveStage, undo, redo, clearHistory })(StageEditPage);
