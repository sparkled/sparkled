import { format } from 'date-fns';
import React from 'react';

export default ({ date }) => {

  return (
    <span>{formatDateTime(date)}</span>
  );
};

function formatDateTime(date) {
  return date == null ? '' : format(date, 'hh:mm:ss A');
}
