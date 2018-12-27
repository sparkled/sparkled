import MuiTextField from '@material-ui/core/TextField';
import React from 'react';

let inputId = 0;

const NON_BREAKING_SPACE = ' ';

const TextField = ({ input, disabled, required, min, max, label, type, meta, fullWidth }) => {
  const hasError = meta.touched && Boolean(meta.error);
  const attrs = { disabled, required, min, max, fullWidth, error: hasError };

  const id = 'TextField-' + (inputId++);

  const errorText = hasError ? meta.error : null;
  const helperText = errorText || NON_BREAKING_SPACE;

  return (
    <MuiTextField
      id={id}
      label={label}
      helperText={helperText}
      type="text"
      margin="dense"
      {...input}
      {...attrs}
    />
  );
};

export default TextField;
