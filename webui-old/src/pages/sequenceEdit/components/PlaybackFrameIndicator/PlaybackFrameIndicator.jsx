import React, { Component } from 'react'
import { connect } from 'react-redux'
import { eventType, subscribe, unsubscribe } from '../../../../utils/eventBus'
import './PlaybackFrameIndicator.css'

class PlaybackFrameIndicator extends Component {
  constructor(props) {
    super(props)
    this.ref = React.createRef()
  }

  componentDidMount() {
    subscribe(eventType.RENDER_DATA, this.updateIndicator)
  }

  componentWillReceiveProps(nextProps, nextContext) {
    if (!nextProps.renderData) {
      this.updateIndicator({ renderData: null })
    }
  }

  render() {
    const style = {
      width: this.props.pixelsPerFrame,
      display: 'none'
    }

    return (
      <div className="PlaybackFrameIndicator" style={style} ref={this.ref} />
    )
  }

  componentWillUnmount() {
    unsubscribe(eventType.RENDER_DATA, this.updateIndicator)
  }

  updateIndicator = data => {
    const node = this.ref.current

    if (data) {
      node.style.display = ''
      node.style.left = `${data.playbackFrame * this.props.pixelsPerFrame}px`
    } else {
      node.style.display = 'none'
    }
  }
}

function mapStateToProps({ page }) {
  const { present } = page.sequenceEdit
  const { currentFrame, pixelsPerFrame, renderData } = present
  return { currentFrame, pixelsPerFrame, renderData }
}

export default connect(
  mapStateToProps,
  {}
)(PlaybackFrameIndicator)
