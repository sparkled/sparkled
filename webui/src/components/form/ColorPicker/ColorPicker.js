import React from 'react';
import { CompactPicker } from 'react-color';
import './ColorPicker.css';

class ColorPicker extends React.Component {
  state = { displayColorPicker: false, color: {} };

  constructor(props) {
    super(props);
    this.openColorPicker = this.openColorPicker.bind(this);
    this.closeColorPicker = this.closeColorPicker.bind(this);
    this.changeColor = this.changeColor.bind(this);
  }

  render() {
    const { input, className = '', disabled, required, label, meta } = this.props;
    const hasError = meta.touched && meta.error;
    const formGroupClass = hasError ? 'form-group has-danger' : 'form-group';
    const fieldClass = hasError ? 'is-invalid' : '';
    const errorContent = hasError ? <div className="invalid-feedback">{meta.error}</div> : null;
    const attrs = { disabled, required };

    const colorPicker = !this.state.displayColorPicker ? null : (
      <div className="popover">
        <div className="cover" onClick={this.closeColorPicker}/>
        <CompactPicker {...input} { ...attrs } color={this.state.color} onChange={this.changeColor}/>
      </div>
    );

    return (
      <div className={`ColorPicker ${formGroupClass} ${className}`}>
        <label className="form-control-label">{label} {required ? '*' : ''}</label>
        <div className="swatch-container">
          <div className={"swatch " + fieldClass} onClick={this.openColorPicker}>
            <div className="color" style={{ background: input.value }}/>
          </div>
          {colorPicker}
          {errorContent}
        </div>
      </div>
    );
  }

  openColorPicker() {
    this.setState({ displayColorPicker: !this.state.displayColorPicker });
  };

  closeColorPicker() {
    this.props.input.onChange(this.state.color.hex);
    this.setState({ displayColorPicker: false });
  };

  changeColor(color) {
    this.setState({ color });
  };
}

export default ColorPicker;

