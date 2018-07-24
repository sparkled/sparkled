import React from 'react';
import largeLogo from './logo-large.svg';
import smallLogo from './logo-small.svg';
import './AppLogo.css';

export default () => [
  <img key="small" className="app-logo d-lg-none" src={smallLogo} alt="Sparkled"/>,
  <img key="large" className="app-logo d-none d-lg-inline-block" src={largeLogo} alt="Sparkled"/>
];
