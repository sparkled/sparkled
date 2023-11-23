import React from 'react'
import './InputField.css'

let inputId = 0

const InputField = ({ input, className = '', disabled, required, min, max, label, type, meta }) => {
  const hasError = meta.touched && meta.error
  const formGroupClass = hasError ? 'form-group has-danger' : 'form-group'
  const fieldClass = hasError ? 'form-control is-invalid' : 'form-control'
  const errorContent = hasError ? <div className='invalid-feedback'>{meta.error}</div> : null
  const attrs = { disabled, required, min, max }

  const id = 'InputField-' + inputId++
  return (
    <div className={`InputField ${className} ${formGroupClass}`}>
      <label className='form-control-label' htmlFor={id}>
        {label} {required ? '*' : ''}
      </label>
      <input id={id} className={fieldClass} type={type} {...input} {...attrs} checked={input.value === true} />
      {errorContent}
    </div>
  )
}

export default InputField
