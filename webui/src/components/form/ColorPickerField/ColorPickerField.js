import React from 'react'
import ColorPicker from '../../ColorPicker/ColorPicker'

class ColorPickerField extends React.Component {
  constructor(props) {
    super(props)
    this.onChange = this.onChange.bind(this)
  }

  render() {
    const { input, className = '', required, label, meta } = this.props
    const hasError = meta.touched && meta.error
    const formGroupClass = hasError ? 'form-group has-danger' : 'form-group'
    const fieldClass = hasError ? 'is-invalid' : ''
    const errorContent = hasError ? (
      <div className="invalid-feedback">{meta.error}</div>
    ) : null

    return (
      <div className={`ColorPickerField ${formGroupClass} ${className}`}>
        <label className="form-control-label">
          {label} {required ? '*' : ''}
        </label>
        <div className={`d-flex ${fieldClass}`}>
          <ColorPicker color={input.value} onChange={this.onChange} />
        </div>

        {errorContent}
      </div>
    )
  }

  onChange(color) {
    this.props.input.onChange(color.hex)
  }
}

export default ColorPickerField
