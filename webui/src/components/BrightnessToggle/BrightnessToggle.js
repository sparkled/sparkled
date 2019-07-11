import { IconButton, Popover, Typography, Slider } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import BrightnessIcon from '@material-ui/icons/WbSunny';
import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchBrightness, updateBrightness } from '../../pages/actions';

const anchorOrigin = { vertical: 'bottom', horizontal: 'left' };

const styles = () => ({
  icon: {
    cursor: 'pointer'
  },
  popoverContainer: {
    padding: '8px 30px'
  },
  label: {
    marginTop: 8,
    textAlign: 'center',
    marginBottom: 8
  },
  slider: {
    width: 200,
    padding: '22px 0px'
  }
});

class BrightnessToggle extends Component {

  state = {
    anchorEl: null
  };

  constructor(props) {
    super(props);
    props.fetchBrightness();
  }

  componentWillUpdate(nextProps, nextState) {
    if (!this.state.brightness && nextProps.brightness) {
      this.setState({ brightness: nextProps.brightness });
    }
  }

  render() {
    const { brightness, id, anchorEl } = this.state;
    const { classes } = this.props;

    const hasBrightness = brightness !== null;
    const isOpen = Boolean(anchorEl) && hasBrightness;

    return (
      <>
        <IconButton onClick={this.openPopover}>
          <BrightnessIcon id={id} className={classes.icon}/>
        </IconButton>
        <Popover open={isOpen} anchorEl={anchorEl} onClose={this.closePopover} anchorOrigin={anchorOrigin}>
          <div className={classes.popoverContainer}>
            <Typography className={classes.label}>Adjust global brightness</Typography>

            <Slider min={0} max={15} step={1} value={brightness} onChange={this.updateBrightness}/>
          </div>
        </Popover>
      </>
    );
  }

  openPopover = event => {
    this.setState({ anchorEl: event.currentTarget });
  };

  closePopover = () => {
    this.setState({ anchorEl: null });
  };

  updateBrightness = (event, brightness) => {
    this.setState({ brightness });
    this.updateServerBrightness(brightness);
  }

  updateServerBrightness = _.debounce(brightness => {
    this.props.updateBrightness(brightness);
  }, 200);
}

function mapStateToProps({ page }) {
  const { brightness } = page.shared;
  return { brightness };
}

BrightnessToggle = connect(mapStateToProps, { fetchBrightness, updateBrightness })(BrightnessToggle);
export default withStyles(styles)(BrightnessToggle);
