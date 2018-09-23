import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import SplitPane from 'react-split-pane';
import { Nav, NavItem } from 'reactstrap';
import { ActionCreators } from 'redux-undo';
import uuidv4 from 'uuid/v4';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { setCurrentPage } from '../actions';
import StageCanvas from '../StageEdit/components/StageCanvas';
import {
  addEffect,
  fetchReferenceData,
  fetchSequence,
  fetchSequenceStage,
  saveSequence,
  showAddChannelModal
} from './actions';
import AddChannelModal from './components/AddChannelModal';
import EffectForm from './components/EffectForm';
import Timeline from './components/Timeline';
import './SequenceEditPage.css';

const { undo, redo, clearHistory } = ActionCreators;

const newEffectFrames = 10;

class SequenceEditPage extends Component {

  constructor(props) {
    super(props);
    this.addEffect = this.addEffect.bind(this);
  }

  componentDidMount() {
    const { setCurrentPage, fetchSequenceStage, fetchSequence, fetchReferenceData } = this.props;
    const { sequenceId } = this.props.match.params;
    setCurrentPage({ pageTitle: 'Edit Sequence', pageClass: 'sequence-edit-page' });
    fetchSequenceStage(sequenceId);
    fetchSequence(sequenceId);
    fetchReferenceData();
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
    const { canUndo, canRedo, undo, redo, sequence, stage, selectedChannel } = this.props;
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
          <span className="nav-link" onClick={this.props.showAddChannelModal}>Add Channel</span>
        </NavItem>
        <NavItem className={(loaded && selectedChannel) ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.addEffect}>Add Effect</span>
        </NavItem>
        <NavItem className={loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => this.props.saveSequence(sequence)}>Save</span>
        </NavItem>
      </Nav>
    );
  }

  addEffect() {
    const { addEffect, currentFrame, sequence } = this.props;
    const effect = {
      uuid: uuidv4(),
      type: 'FLASH',
      params: [],
      easing: {
        type: 'LINEAR',
        params: []
      },
      fill: {
        type: 'SOLID',
        params: [
          { name: 'COLOR', type: 'COLOR', value: ['#ffffff'] }
        ]
      },
      startFrame: currentFrame,
      endFrame: Math.min(currentFrame + newEffectFrames, sequence.durationFrames) - 1,
      repetitions: 1,
      reverse: false
    };

    addEffect(effect);
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
          <SplitPane split="vertical" primary="second" defaultSize={300} allowResize={false}
                     pane1ClassName="stage-canvas-container">
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
  const { saving, saveError, sequence, stage, currentFrame, selectedChannel } = present;

  return {
    saving, saveError, sequence, stage, currentFrame, selectedChannel,
    fetching: present.fetchingSequence || present.fetchingStage || present.fetchingReferenceData,
    fetchError: present.fetchSequenceError || present.fetchStageError || present.fetchReferenceDataError,
    canUndo: past.length > 1,
    canRedo: future.length > 0
  };
}

export default connect(mapStateToProps, {
  setCurrentPage,
  fetchSequence,
  fetchSequenceStage,
  fetchReferenceData,
  addEffect,
  showAddChannelModal,
  saveSequence,
  undo,
  redo,
  clearHistory
})(SequenceEditPage);
