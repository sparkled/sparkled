import React from 'react';
import { CompactPicker } from 'react-color';
import './ColorPicker.css';

class ColorPicker extends React.Component {
  state = { displayColorPicker: false, color: null };

  constructor(props) {
    super(props);
    this.openColorPicker = this.openColorPicker.bind(this);
    this.closeColorPicker = this.closeColorPicker.bind(this);
    this.changeColor = this.changeColor.bind(this);
  }

  render() {
    const { color, className = '', onDelete } = this.props;

    const colorPicker = !this.state.displayColorPicker ? null : (
      <div className="popover">
        <div className="cover" onClick={this.closeColorPicker}/>
        <CompactPicker color={color} onChange={this.changeColor}/>
      </div>
    );

    const deleteButton = onDelete ? <div className="delete-color" onClick={onDelete} title="Delete color">⨯</div> : [];

    return (
      <div className={`ColorPicker ${className}`}>
        <div className="swatch-container">
          <div className="swatch" onClick={this.openColorPicker}>
            <div className="color" style={{ background: color }}/>
            {deleteButton}
          </div>
          {colorPicker}
        </div>
      </div>
    );
  }

  openColorPicker() {
    this.setState({ displayColorPicker: !this.state.displayColorPicker });
  };

  closeColorPicker() {
    if (this.state.color) {
      this.props.onChange(this.state.color);
    }
    this.setState({ displayColorPicker: false });
  };

  changeColor(color) {
    this.setState({ color });
    this.props.onChange(color);
  };
}

export default ColorPicker;

