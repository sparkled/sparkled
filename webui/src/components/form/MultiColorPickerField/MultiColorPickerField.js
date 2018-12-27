import _ from 'lodash';
import React from 'react';
import ColorPicker from '../../ColorPicker/ColorPicker';
import './MultiColorPickerField.css';
import { Button } from 'reactstrap';

class MultiColorPickerField extends React.Component {

  constructor(props) {
    super(props);
    this.renderColorPickers = this.renderColorPickers.bind(this);
    this.addColor = this.addColor.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onDelete = this.onDelete.bind(this);
  }

  render() {
    const { className = '', required, label, meta } = this.props;
    const hasError = meta.touched && meta.error;
    const formGroupClass = hasError ? 'form-group has-danger' : 'form-group';
    const fieldClass = hasError ? 'is-invalid' : '';
    const errorContent = hasError ? <div className="invalid-feedback">{meta.error}</div> : null;

    return (
      <div className={`MultiColorPickerField ${formGroupClass} ${className}`}>
        <label className="form-control-label">{label} {required ? '*' : ''}</label>
        <div className={`color-pickers ${fieldClass}`}>
          {this.renderColorPickers()}
          <Button className="add-color" color="info" size="sm" onClick={this.addColor}>+</Button>
        </div>

        {errorContent}
      </div>
    );
  }

  renderColorPickers() {
    const { value } = this.props.input;

    return _.map(value, (color, index) => {
      // Prevent deletion of the first color.
      const onDelete = index === 0 ? null : () => this.onDelete(index);

      return (
        <ColorPicker key={index} color={color}
                   onChange={color => this.onChange(color, index)}
                   onDelete={onDelete}/>
      );
    });
  }

  addColor() {
    const { value, onChange } = this.props.input;

    const newColors = [...value, '#ff0000'];
    onChange(newColors);
  }

  onChange(color, index) {
    const { value, onChange } = this.props.input;

    const newColors = [...value];
    newColors[index] = color.hex;
    onChange(newColors);
  }

  onDelete(index) {
    const { value, onChange } = this.props.input;

    const newColors = [...value];
    newColors.splice(index, 1);
    onChange(newColors);
  }
}

export default MultiColorPickerField;

