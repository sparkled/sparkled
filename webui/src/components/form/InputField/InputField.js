import React from 'react';

const InputField = ({ input, className = '', disabled, required, label, name, type, meta }) => {
  const hasError = meta.touched && meta.error;
  const formGroupClass = hasError ? 'form-group has-danger' : 'form-group';
  const fieldClass = hasError ? 'form-control is-invalid' : 'form-control';
  const errorContent = hasError ? <div className="invalid-feedback">{meta.error}</div> : null;
  const attrs = { disabled, required };

  return (
    <div className={`${className} ${formGroupClass}`}>
      <label className="form-control-label" htmlFor={name}>{label} {required ? '*' : ''}</label>
      <input {...input} {...attrs} className={fieldClass} type={type}/>
      {errorContent}
    </div>
  );
};

export default InputField;
