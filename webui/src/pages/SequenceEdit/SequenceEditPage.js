import Slider from '@material-ui/lab/Slider';
import Mousetrap from 'mousetrap';
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
import * as sequenceStatuses from '../SequenceList/sequenceStatuses';
import StageCanvas from '../StageEdit/components/StageCanvas';
import {
  addEffect,
  adjustPlaybackSpeed,
  adjustPreviewDuration,
  cancelRender,
  copyEffect,
  deleteEffect,
  fetchReferenceData,
  fetchSequence,
  fetchSequenceStage,
  pasteEffect,
  previewRender,
  saveSequence,
  showAddChannelModal
} from './actions';
import AddChannelModal from './components/AddChannelModal';
import EffectForm from './components/EffectForm';
import Timeline from './components/Timeline';
import { PlaybackSpeeds } from './playbackSpeeds';
import './SequenceEditPage.css';

const { undo, redo, clearHistory } = ActionCreators;

const NEW_EFFECT_FRAMES = 10;

class SequenceEditPage extends Component {

  componentDidMount() {
    const { setCurrentPage, fetchSequenceStage, fetchSequence, fetchReferenceData } = this.props;
    const { sequenceId } = this.props.match.params;
    setCurrentPage({ pageTitle: 'Edit Sequence', pageClass: 'SequenceEditPage' });
    fetchSequenceStage(sequenceId);
    fetchSequence(sequenceId);
    fetchReferenceData();

    Mousetrap.bind('mod+c', this.props.copyEffect);
    Mousetrap.bind('mod+v', this.props.pasteEffect);
    Mousetrap.bind('del', this.deleteSelectedEffect);
    Mousetrap.bind('mod+z', this.undo);
    Mousetrap.bind('mod+shift+z', this.redo);
    Mousetrap.bind('ctrl+space', this.previewRender);
    Mousetrap.bind('esc', this.props.cancelRender);
  }

  deleteSelectedEffect = () => {
    const { deleteEffect, selectedChannel, selectedEffect } = this.props;
    if (selectedChannel && selectedEffect) {
      deleteEffect(selectedChannel, selectedEffect);
    }
  }

  undo = () => {
    const { canUndo, undo } = this.props;
    canUndo && undo();
  }

  redo = () => {
    const { canRedo, redo } = this.props;
    canRedo && redo();
  }

  previewRender = () => {
    const { currentFrame, previewDuration, previewRender, sequence } = this.props;
    previewRender(sequence, currentFrame, sequence.framesPerSecond * previewDuration);
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
    const { canUndo, canRedo, undo, redo, sequence, stage, selectedChannel, saving, previewDuration, playbackSpeed } = this.props;
    const loaded = sequence && stage;

    return (
      <Nav className="ml-auto" navbar>

        <NavItem className={(!saving && loaded) ? 'd-flex align-items-center mr-3' : 'd-none'}>
          <input type="number" min={1} style={{ width: 50 }} value={previewDuration}
                 onChange={this.adjustPreviewDuration}/>
        </NavItem>
        <NavItem className={(!saving && loaded) ? 'd-flex align-items-center mr-3' : 'd-none'}>
          <Slider min={0} max={PlaybackSpeeds.length - 1} value={playbackSpeed} style={{ width: 50 }}
                  onChange={this.adjustPlaybackSpeed}/>
        </NavItem>

        <NavItem className={(!saving && loaded && canUndo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => undo()}>Undo</span>
        </NavItem>
        <NavItem className={(!saving && loaded && canRedo) ? '' : 'd-none'}>
          <span className="nav-link" onClick={() => redo()}>Redo</span>
        </NavItem>
        <NavItem className={!saving && loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.props.showAddChannelModal}>Add Channel</span>
        </NavItem>
        <NavItem className={(!saving && loaded && selectedChannel) ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.addEffect}>Add Effect</span>
        </NavItem>
        <NavItem className={!saving && loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.saveSequence}>Save</span>
        </NavItem>
        <NavItem className={!saving && loaded ? '' : 'd-none'}>
          <span className="nav-link" onClick={this.publishSequence}>Publish</span>
        </NavItem>
      </Nav>
    );
  }

  adjustPreviewDuration = event => {
    const duration = Math.max(1, Number(event.target.value));
    this.props.adjustPreviewDuration(duration);
  }

  adjustPlaybackSpeed = speedIndex => {
    this.props.adjustPlaybackSpeed(speedIndex);
  }

  saveSequence = () => {
    const { sequence, saveSequence } = this.props;
    saveSequence({ ...sequence, status: sequenceStatuses.DRAFT });
  }

  publishSequence = () => {
    const { sequence, saveSequence } = this.props;
    saveSequence({ ...sequence, status: sequenceStatuses.PUBLISHED });
  }

  addEffect = () => {
    const { addEffect, currentFrame, sequence } = this.props;
    const effect = {
      uuid: uuidv4(),
      type: 'FLASH',
      args: {},
      easing: {
        type: 'LINEAR',
        start: 0,
        end: 100,
        args: {}
      },
      fill: {
        type: 'SOLID',
        args: {
          'COLOR': ['#ff0000']
        }
      },
      startFrame: currentFrame,
      endFrame: Math.min(currentFrame + NEW_EFFECT_FRAMES, sequence.frameCount) - 1,
      repetitions: 1,
      repetitionSpacing: 0,
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
    const { sequence, stage, stageProps, pixelsPerFrame } = this.props;
    return (
      <Fragment>
        <SplitPane split="horizontal" minSize={100} defaultSize={200} primary="second">
          <SplitPane split="vertical" primary="second" defaultSize={300} allowResize={false}
                     pane1ClassName="stage-canvas-container">
            <StageCanvas stage={this.props.stage} editable={false}/>
            <EffectForm/>
          </SplitPane>

          <Timeline sequence={sequence} stage={stage} stageProps={stageProps} pixelsPerFrame={pixelsPerFrame}/>
        </SplitPane>

        <AddChannelModal/>
      </Fragment>
    );
  }

  componentWillUnmount() {
    this.props.clearHistory();
    Mousetrap.unbind('del');
    Mousetrap.unbind('mod+c');
    Mousetrap.unbind('mod+v');
    Mousetrap.unbind('mod+z');
    Mousetrap.unbind('mod+shift+z');
    Mousetrap.unbind('ctrl+space');
    Mousetrap.unbind('esc');
  }
}

function mapStateToProps({ page }) {
  const { past, present, future } = page.sequenceEdit;
  const { saving, saveError, sequence, stage, currentFrame, previewDuration, playbackSpeed, selectedChannel, selectedEffect, pixelsPerFrame } = present;

  return {
    saving,
    saveError,
    sequence,
    stage,
    currentFrame,
    previewDuration,
    playbackSpeed,
    selectedChannel,
    selectedEffect,
    pixelsPerFrame,
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
  cancelRender,
  copyEffect,
  pasteEffect,
  previewRender,
  adjustPreviewDuration,
  adjustPlaybackSpeed,
  deleteEffect,
  showAddChannelModal,
  saveSequence,
  undo,
  redo,
  clearHistory
})(SequenceEditPage);
