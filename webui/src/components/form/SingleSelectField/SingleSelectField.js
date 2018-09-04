import _ from 'lodash';
import React from 'react';

const SingleSelectField = ({ input, options, className = '', disabled, required, label, name, meta }) => {
  const hasError = meta.touched && meta.error;
  const formGroupClass = hasError ? 'form-group has-danger' : 'form-group';
  const fieldClass = hasError ? 'form-control is-invalid' : 'form-control';
  const errorContent = hasError ? <div className="invalid-feedback">{meta.error}</div> : null;
  const attrs = { disabled, required };

  const optionElements = _.map(options, option => (
    <option key={option.id} value={option.id}>{option.name}</option>
  ));


  return (
    <div className={`${className} ${formGroupClass}`}>
      <label className="form-control-label" htmlFor={name}>{label} {required ? '*' : ''}</label>
      <select {...input} {...attrs} className={fieldClass}>
        <option/>
        {optionElements}
      </select>
      {errorContent}
    </div>
  );
};

export default SingleSelectField;
