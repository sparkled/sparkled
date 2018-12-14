import _ from 'lodash';
import Slider from 'rc-slider';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Popover, PopoverBody } from 'reactstrap';
import sun from '../../images/sun.svg';
import { fetchBrightness, updateBrightness } from '../../pages/actions';
import './BrightnessToggle.css';
import 'rc-slider/dist/rc-slider.css';

class BrightnessToggle extends Component {

  state = {
    id: `BrightnessToggle-${+new Date()}`,
    popoverOpen: false
  };

  componentWillUpdate(nextProps, nextState) {
    const { popoverOpen } = nextState;
    if (!this.state.popoverOpen && popoverOpen) {
      nextProps.fetchBrightness();
    }
  }

  render() {
    const { id, popoverOpen } = this.state;
    const { brightness } = this.props;

    return (
      <>
        <img src={sun} id={id} className="BrightnessToggle" alt="Adjust Brightness" onClick={this.togglePopover}/>

        <Popover placement="bottom" isOpen={popoverOpen && brightness !== null} target={id}
                 className="BrightnessPopover" toggle={this.togglePopover}>
          <PopoverBody>
            <Slider min={0} max={15} defaultValue={brightness} vertical={true} onChange={this.updateBrightness}/>
          </PopoverBody>
        </Popover>
      </>
    );
  }

  togglePopover = () => {
    this.setState({ popoverOpen: !this.state.popoverOpen });
  }

  updateBrightness = _.debounce(brightness => {
    this.props.updateBrightness(brightness);
  }, 200);
}

function mapStateToProps({ page }) {
  const { brightness } = page.shared;
  return { brightness };
}

export default connect(mapStateToProps, { fetchBrightness, updateBrightness })(BrightnessToggle);
