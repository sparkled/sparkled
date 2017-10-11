import React from 'react';
import { Link } from 'react-router-dom';

export default () => (
  <div>
    <h2>Home</h2>

    <ul>
      <li><Link to='/'>Home</Link></li>
      <li><Link to='/scheduler'>Scheduler</Link></li>
      <li><Link to='/songs'>Song List</Link></li>
    </ul>
  </div>
);
