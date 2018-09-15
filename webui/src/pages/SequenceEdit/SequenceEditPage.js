import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { ActionCreators } from 'redux-undo';
import Alert from 'react-s-alert';
import SplitPane from 'react-split-pane';
import { Nav, NavItem } from 'reactstrap';
import { setCurrentPage } from '../actions';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import StageCanvas from '../StageEdit/components/StageCanvas';
import { fetchSequence, fetchSequenceStage, showAddChannelModal, saveSequence } from './actions';
import AddChannelModal from './components/AddChannelModal';
import Timeline from './components/Timeline';
import EffectForm from './components/EffectForm';
import './SequenceEditPage.css';

const { undo, redo, clearHistory } = ActionCreators;

class SequenceEditPage extends Component {

  componentDidMount() {
    const { sequenceId } = this.props.match.params;
    this.props.setCurrentPage({ pageTitle: 'Edit Sequence', pageClass: 'sequence-edit-page' });
    this.props.fetchSequenceStage(sequenceId);
    this.props.fetchSequence(sequenceId);
  }

  componentWillReceiveProps(nextProps) {
    const didSave = this.props.saving && !nextProps.saving;
    if (didSave) {
      const saveError = nextProps.saveError;
      if (saveError) {
        Alert.error(`Save failed: ${saveError}`);
      } else {
        Alert.success('Sequence saved successfully');
      }
    }
  }

  render() {
    const pageBody = (
      <div className="d-flex w-100 h-100">
        {this.renderContent()}
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()} className="sequence-editor"/>;
  }

  renderNavbar() {
    const { canUndo, canRedo, undo, redo, sequence, stage } = this.props;
    const loaded = sequence && stage;

    return (
      <Nav className="ml-auto" navbar>
        <NavItem className={(loaded && canUndo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => undo()}>Undo</span>
        </NavItem>
        <NavItem className={(loaded && canRedo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => redo()}>Redo</span>
        </NavItem>
        <NavItem className={loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => this.props.showAddChannelModal()}>Add Channel</span>
        </NavItem>
        <NavItem className={loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => this.props.saveSequence(sequence)}>Save</span>
        </NavItem>
      </Nav>
    );
  }

  renderContent() {
    const { fetchError, fetching, sequence, stage } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderError();
    } else if (sequence && stage) {
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
          <p>Failed to load page: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderEditor() {
    return (
      <Fragment>
        <SplitPane split="horizontal" minSize={100} defaultSize={200} primary="second">
          <SplitPane split="vertical" primary="second" defaultSize={300} allowResize={false} pane1ClassName="stage-canvas-container">
            <StageCanvas stage={this.props.stage} editable={false}/>
            <EffectForm/>
          </SplitPane>

          <Timeline/>
        </SplitPane>

        <AddChannelModal/>
      </Fragment>
    );
  }

  componentWillUnmount() {
    this.props.clearHistory();
  }
}

function mapStateToProps({ page }) {
  const { past, present, future } = page.sequenceEdit;

  return {
    fetching: present.fetchingSequence || present.fetchingStage,
    fetchError: present.fetchSequenceError || present.fetchStageError,
    saving: present.saving,
    saveError: present.saveError,
    sequence: present.sequence,
    stage: present.stage,
    canUndo: past.length > 1,
    canRedo: future.length > 0
  };
}

export default connect(mapStateToProps, {
  setCurrentPage, fetchSequence, fetchSequenceStage, showAddChannelModal, saveSequence, undo, redo, clearHistory
})(SequenceEditPage);
