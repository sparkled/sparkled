import React from 'react';
import { Link } from 'react-router-dom';

export default ({ match }) => (
  <div>
    <h2>Song List</h2>

    <ul>
      <li>
        <Link to={`${match.url}/1`}>
          Edit song 1
        </Link>
      </li>
      <li>
        <Link to={`${match.url}/2`}>
          Edit song 2
        </Link>
      </li>
    </ul>
  </div>
);
