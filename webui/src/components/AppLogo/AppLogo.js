import React from 'react';
import largeLogo from './logo-large.svg';
import smallLogo from './logo-small.svg';
import './AppLogo.css';

export default () => [
  <img key="small" className="app-logo d-inline-block hidden-lg-up" src={smallLogo} alt="Sparkled"/>,
  <img key="large" className="app-logo d-inline-block hidden-md-down" src={largeLogo} alt="Sparkled"/>
];
