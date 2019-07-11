import { TextField } from '@material-ui/core';
import React from 'react';

let inputId = 0;

const NON_BREAKING_SPACE = ' ';

export default ({ input, disabled, required, min, max, label, meta, fullWidth }) => {
  const hasError = meta.touched && Boolean(meta.error);
  const attrs = { disabled, required, min, max, fullWidth, error: hasError };

  const id = 'TextField-' + (inputId++);

  const errorText = hasError ? meta.error : null;
  const helperText = errorText || NON_BREAKING_SPACE;

  return (
    <TextField
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
