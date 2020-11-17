import _ from 'lodash'
import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Rnd } from 'react-rnd'
import { selectEffect, updateEffect } from '../../actions'
import './TimelineEffect.css'

class TimelineEffect extends Component {
  constructor(props) {
    super(props)
    this.rndRef = React.createRef()
  }

  shouldComponentUpdate(nextProps) {
    const { effect, selectedEffect } = this.props
    const effectChanged = effect !== nextProps.effect
    const selectedEffectChanged = (effect === selectedEffect) !== (nextProps.effect === nextProps.selectedEffect)

    return effectChanged || selectedEffectChanged
  }

  componentWillReceiveProps(nextProps) {
    if (this.rndRef.current) {
      const dimensions = this.getDimensions(nextProps)
      this.rndRef.current.updateSize(dimensions)
      this.rndRef.current.updatePosition(dimensions)
    }
  }

  render() {
    const dimensions = this.getDimensions(this.props)

    return (
      <>
        <Rnd
          className={'TimelineEffect ' + this.getEffectClass()}
          ref={this.rndRef}
          bounds='parent'
          default={dimensions}
          enableResizing={{ left: true, right: true }}
          dragAxis='x'
          dragGrid={[2, 0]}
          resizeGrid={[2, 0]}
          onMouseDown={this.onEffectClick}
          onDragStart={this.selectEffect}
          onDragStop={this.onEffectMove}
          onResizeStart={this.selectEffect}
          onResizeStop={this.onEffectResize}
        />
        {this.renderRepetitions()}
      </>
    )
  }

  getEffectClass() {
    const { effect, selectedEffect } = this.props
    if (selectedEffect && effect.uuid === selectedEffect.uuid) {
      return 'Active'
    } else if (effect.invalid) {
      return 'Invalid'
    } else {
      return ''
    }
  }

  getDimensions(props) {
    const { effect } = props
    const x = effect.startFrame * 2
    const width = (effect.endFrame - effect.startFrame + 1) * 2
    return { width, height: 'inherit', x, y: 0 }
  }

  onEffectMove = (event, data) => {
    const { effect } = this.props
    const startFrame = Math.floor(data.x / 2)
    const endFrame = Math.round(startFrame + (effect.endFrame - effect.startFrame))

    this.moveEffect(startFrame, endFrame)
  }

  onEffectResize = (event, data, ref, delta) => {
    const { effect } = this.props
    const isLeft = data === 'left'

    let startFrame = effect.startFrame
    let endFrame = effect.endFrame

    if (isLeft) {
      startFrame = Math.floor(effect.startFrame - delta.width / 2)
    } else {
      endFrame = Math.round(effect.endFrame + delta.width / 2)
    }

    this.moveEffect(startFrame, endFrame)
  }

  moveEffect = (startFrame, endFrame) => {
    const { channel, effect, sequence } = this.props

    startFrame = Math.max(startFrame, 0)
    endFrame = Math.min(endFrame, sequence.frameCount - 1)

    if (effect.startFrame !== startFrame || effect.endFrame !== endFrame) {
      this.props.updateEffect(channel, { ...effect, startFrame, endFrame })
    }
  }

  onEffectClick = event => {
    this.selectEffect()
    event.stopPropagation() // Prevent channel click handler from firing and deselecting the effect.
  }

  selectEffect = () => {
    const { channel, effect } = this.props
    this.props.selectEffect(channel, effect)
  }

  renderRepetitions() {
    const { effect, pixelsPerFrame } = this.props
    const { endFrame, repetitions = 1, repetitionSpacing = 0, startFrame, uuid } = effect
    if (repetitions > 1) {
      const duration = endFrame - startFrame + 1
      const width = duration * pixelsPerFrame

      return _.map(Array(repetitions - 1), (_, i) => {
        const left = (startFrame + (duration + repetitionSpacing) * (i + 1)) * pixelsPerFrame
        return <div key={`${uuid}-${i}`} className='TimelineEffectRepetition' style={{ left, width }} />
      })
    } else {
      return []
    }
  }
}

function mapStateToProps({ page }) {
  const { pixelsPerFrame, sequence, selectedEffect } = page.sequenceEdit.present
  return { pixelsPerFrame, sequence, selectedEffect }
}

export default connect(mapStateToProps, { selectEffect, updateEffect })(TimelineEffect)
