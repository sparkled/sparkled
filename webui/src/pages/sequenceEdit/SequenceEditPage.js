import Mousetrap from 'mousetrap'
import React, { Component, Fragment } from 'react'
import { connect } from 'react-redux'
import Alert from 'react-s-alert'
import SplitPane from 'react-split-pane'
import { ActionCreators } from 'redux-undo'
import uuidv4 from 'uuid/v4'
import LoadingIndicator from '../../components/LoadingIndicator'
import PageContainer from '../../components/PageContainer'
import { setCurrentPage } from '../actions'
import * as sequenceStatuses from '../sequenceList/sequenceStatuses'
import StageEditor from '../../components/stageEditor'
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
} from './actions'
import AddChannelModal from './components/AddChannelModal'
import EffectForm from './components/EffectForm'
import Timeline from './components/Timeline'
import './SequenceEditPage.css'
import { IconButton, withStyles } from '@material-ui/core'
import {
  Redo,
  Undo,
  Save,
  Publish,
  Layers,
  Star,
  PlayArrow
} from '@material-ui/icons'

const { undo, redo, clearHistory } = ActionCreators

const NEW_EFFECT_FRAMES = 10

const styles = () => ({
  pageContainer: {
    padding: 0
  }
})

class SequenceEditPage extends Component {
  componentDidMount() {
    const {
      setCurrentPage,
      fetchSequenceStage,
      fetchSequence,
      fetchReferenceData
    } = this.props
    const { sequenceId } = this.props.match.params
    setCurrentPage({
      pageTitle: 'Edit Sequence',
      pageClass: 'SequenceEditPage'
    })
    fetchSequenceStage(sequenceId)
    fetchSequence(sequenceId)
    fetchReferenceData()

    Mousetrap.bind('mod+c', this.props.copyEffect)
    Mousetrap.bind('mod+v', this.props.pasteEffect)
    Mousetrap.bind('del', this.deleteSelectedEffect)
    Mousetrap.bind('mod+z', this.undo)
    Mousetrap.bind('mod+shift+z', this.redo)
    Mousetrap.bind('ctrl+space', this.previewRender)
    Mousetrap.bind('esc', this.props.cancelRender)
  }

  deleteSelectedEffect = () => {
    const { deleteEffect, selectedChannel, selectedEffect } = this.props
    if (selectedChannel && selectedEffect) {
      deleteEffect(selectedChannel, selectedEffect)
    }
  }

  undo = () => {
    const { canUndo, undo } = this.props
    canUndo && undo()
  }

  redo = () => {
    const { canRedo, redo } = this.props
    canRedo && redo()
  }

  previewRender = () => {
    const {
      currentFrame,
      previewDuration,
      previewRender,
      sequence
    } = this.props
    previewRender(
      sequence,
      currentFrame,
      sequence.framesPerSecond * previewDuration
    )
  }

  componentWillReceiveProps(nextProps) {
    const didSave = this.props.saving && !nextProps.saving
    if (didSave) {
      const saveError = nextProps.saveError
      if (saveError) {
        Alert.error(`Save failed: ${saveError}`)
      } else {
        Alert.success('Sequence saved successfully')
      }
    }
  }

  render() {
    const pageBody = (
      <div className="d-flex w-100 h-100">{this.renderContent()}</div>
    )

    return (
      <PageContainer
        actions={this.renderNavbar()}
        className={this.props.classes.pageContainer}
      >
        {pageBody}
      </PageContainer>
    )
  }

  renderNavbar() {
    const {
      canUndo,
      canRedo,
      fetchingRenderData,
      undo,
      redo,
      sequence,
      stage,
      selectedChannel,
      saving,
      previewDuration,
      playbackSpeed
    } = this.props
    const loaded = sequence && stage

    return (
      <>
        <select
          style={{ width: 60, marginRight: 5 }}
          value={previewDuration}
          onChange={this.adjustPreviewDuration}
          title="Preview duration (seconds)"
        >
          <option value="2">2s</option>
          <option value="5">5s</option>
          <option value="10">10s</option>
          <option value="20">20s</option>
          <option value="30">30s</option>
          <option value="60">1m</option>
          <option value="120">2m</option>
          <option value="180">3m</option>
          <option value="240">4m</option>
          <option value="300">5m</option>
        </select>

        <select
          style={{ width: 60, marginRight: 5 }}
          value={playbackSpeed}
          onChange={e => this.adjustPlaybackSpeed(e.target.value)}
          title="Preview speed"
        >
          <option value="25">.25x</option>
          <option value="50">.5x</option>
          <option value="100">1x</option>
          <option value="125">1.25x</option>
          <option value="150">1.5x</option>
          <option value="200">2x</option>
        </select>

        <IconButton
          onClick={() => undo()}
          disabled={saving || !loaded || !canUndo || fetchingRenderData}
          title="Undo"
        >
          <Undo />
        </IconButton>

        <IconButton
          onClick={() => redo()}
          disabled={saving || !loaded || !canRedo || fetchingRenderData}
          title="Redo"
        >
          <Redo />
        </IconButton>

        <IconButton
          onClick={this.props.showAddChannelModal}
          disabled={saving || !loaded || fetchingRenderData}
          title="Add channel"
        >
          <Layers />
        </IconButton>

        <IconButton
          onClick={this.addEffect}
          disabled={saving || !loaded || !selectedChannel || fetchingRenderData}
          title="Add effect"
        >
          <Star />
        </IconButton>

        <IconButton
          onClick={this.saveSequence}
          disabled={saving || !loaded || fetchingRenderData}
          title="Save sequence"
        >
          <Save />
        </IconButton>

        <IconButton
          onClick={this.previewRender}
          disabled={saving || !loaded || fetchingRenderData}
          title="Preview sequence"
        >
          <PlayArrow />
        </IconButton>

        <IconButton
          onClick={this.publishSequence}
          disabled={saving || !loaded || fetchingRenderData}
          title="Publish sequence"
        >
          <Publish />
        </IconButton>
      </>
    )
  }

  adjustPreviewDuration = event => {
    const duration = Math.max(1, Number(event.target.value))
    this.props.adjustPreviewDuration(duration)
  }

  adjustPlaybackSpeed = playbackSpeed => {
    this.props.adjustPlaybackSpeed(playbackSpeed)
  }

  saveSequence = () => {
    const { sequence, saveSequence } = this.props
    saveSequence({ ...sequence, status: sequenceStatuses.DRAFT })
  }

  publishSequence = () => {
    const { sequence, saveSequence } = this.props
    saveSequence({ ...sequence, status: sequenceStatuses.PUBLISHED })
  }

  addEffect = () => {
    const { addEffect, currentFrame, sequence } = this.props
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
          COLOR: ['#ff0000']
        }
      },
      startFrame: currentFrame,
      endFrame:
        Math.min(currentFrame + NEW_EFFECT_FRAMES, sequence.frameCount) - 1,
      repetitions: 1,
      repetitionSpacing: 0,
      reverse: false
    }

    addEffect(effect)
  }

  renderContent() {
    const { fetchError, fetching, sequence, stage } = this.props

    if (fetching) {
      return this.renderLoading()
    } else if (fetchError) {
      return this.renderError()
    } else if (sequence && stage) {
      return this.renderEditor()
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100} />
  }

  renderError() {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>Failed to load page: {this.props.fetchError}</p>
          <button
            className="btn btn-danger"
            onClick={() => window.location.reload()}
          >
            Reload the page
          </button>
        </div>
      </div>
    )
  }

  renderEditor() {
    const { sequence, stage, stageProps, pixelsPerFrame } = this.props
    return (
      <Fragment>
        <SplitPane
          split="horizontal"
          minSize={100}
          defaultSize={200}
          primary="second"
        >
          <SplitPane
            split="vertical"
            primary="second"
            defaultSize={300}
            allowResize={false}
            pane1ClassName="stage-canvas-container"
          >
            <StageEditor stage={this.props.stage} editable={false} />
            <EffectForm />
          </SplitPane>

          <Timeline
            sequence={sequence}
            stage={stage}
            stageProps={stageProps}
            pixelsPerFrame={pixelsPerFrame}
          />
        </SplitPane>

        <AddChannelModal />
      </Fragment>
    )
  }

  componentWillUnmount() {
    this.props.clearHistory()
    Mousetrap.unbind('del')
    Mousetrap.unbind('mod+c')
    Mousetrap.unbind('mod+v')
    Mousetrap.unbind('mod+z')
    Mousetrap.unbind('mod+shift+z')
    Mousetrap.unbind('ctrl+space')
    Mousetrap.unbind('esc')
  }
}

function mapStateToProps({ page }) {
  const { past, present, future } = page.sequenceEdit
  const {
    saving,
    saveError,
    sequence,
    stage,
    currentFrame,
    previewDuration,
    playbackSpeed,
    selectedChannel,
    selectedEffect,
    pixelsPerFrame
  } = present

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
    fetching:
      present.fetchingSequence ||
      present.fetchingStage ||
      present.fetchingReferenceData,
    fetchError:
      present.fetchSequenceError ||
      present.fetchStageError ||
      present.fetchReferenceDataError,
    canUndo: past.length > 1,
    canRedo: future.length > 0
  }
}

SequenceEditPage = connect(
  mapStateToProps,
  {
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
  }
)(SequenceEditPage)
export default withStyles(styles)(SequenceEditPage)
