import React from 'react';

export default ({ match }) => (
  <div>
    <h2>Edit Song { match.params.songId }</h2>
  </div>
);
