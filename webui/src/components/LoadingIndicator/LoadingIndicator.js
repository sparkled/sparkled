import React from 'react';
import './LoadingIndicator.css';
import spinner from '../../images/spinner.svg';

export default ({ size = 100 }) => (
  <div className="loading-indicator">
    <img src={spinner} alt="" style={{width: size, height: size}}/>
  </div>
);
