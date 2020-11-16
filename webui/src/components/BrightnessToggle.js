import { Slider } from '@material-ui/core'
import { withStyles } from '@material-ui/core/styles'
import _ from 'lodash'
import React, { Component } from 'react'
import { connect } from 'react-redux'
import { fetchBrightness, updateBrightness } from '../pages/actions'

const styles = () => ({
  icon: {
    cursor: 'pointer',
  },
  popoverContainer: {
    padding: '8px 30px',
  },
  label: {
    marginTop: 8,
    textAlign: 'center',
    marginBottom: 8,
  },
  slider: {
    width: 200,
    padding: '22px 0px',
  },
})

class BrightnessToggle extends Component {
  state = {
    anchorEl: null,
  }

  constructor(props) {
    super(props)
    props.fetchBrightness()
  }

  componentWillUpdate(nextProps, nextState) {
    if (!this.state.brightness && nextProps.brightness) {
      this.setState({ brightness: nextProps.brightness })
    }
  }

  render() {
    const { brightness } = this.state

    return brightness == null ? null : (
      <Slider min={0} max={15} step={1} value={brightness} onChange={this.updateBrightness} />
    )
  }

  updateBrightness = (event, brightness) => {
    this.setState({ brightness })
    this.updateServerBrightness(brightness)
  }

  updateServerBrightness = _.debounce(brightness => {
    this.props.updateBrightness(brightness)
  }, 200)
}

function mapStateToProps({ page }) {
  const { brightness } = page.shared
  return { brightness }
}

BrightnessToggle = connect(mapStateToProps, { fetchBrightness, updateBrightness })(BrightnessToggle)
export default withStyles(styles)(BrightnessToggle)
