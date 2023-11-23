import _ from 'lodash'
import React from 'react'

const SingleSelectField = ({
  input,
  options,
  allowEmpty = true,
  className = '',
  disabled,
  required,
  label,
  name,
  meta,
}) => {
  const hasError = meta.touched && meta.error
  const formGroupClass = hasError ? 'form-group has-danger' : 'form-group'
  const fieldClass = hasError ? 'form-control is-invalid' : 'form-control'
  const errorContent = hasError ? <div className='invalid-feedback'>{meta.error}</div> : null
  const attrs = { disabled, required }

  const optionElements = _.map(options, option => {
    const id = option.id || option.code
    return (
      <option key={id} value={id}>
        {option.name}
      </option>
    )
  })

  const labelElem = label ? (
    <label className='form-control-label' htmlFor={name}>
      {label} {required ? '*' : ''}
    </label>
  ) : (
    []
  )
  return (
    <div className={`${className} ${formGroupClass}`}>
      {labelElem}
      <select {...input} {...attrs} className={fieldClass}>
        {allowEmpty ? <option /> : []}
        {optionElements}
      </select>
      {errorContent}
    </div>
  )
}

export default SingleSelectField
