import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Collapse, Navbar, NavbarToggler, Nav, NavItem } from 'reactstrap';
import AppLogo from '../AppLogo';
import './PageContainer.css';

class PageContainer extends Component {

  state = { collapsed: false };

  render() {
    const { className = '', body, navbar } = this.props;

    return (
      <div className={className + ' page-container d-flex flex-column h-100'}>
        <Navbar className='navbar-expand-lg navbar-dark bg-dark flex-grow-0 flex-shrink-0'>
          <Link to='/' className='navbar-brand mr-3'>
            <AppLogo/>
          </Link>

          <NavbarToggler onClick={this.toggle.bind(this)} className='navbar-toggler-right'/>

          <Collapse isOpen={this.state.collapsed} navbar>
            <Nav className='mr-auto' navbar>
              <NavItem>
                <Link className='nav-link' to='/'>Songs</Link>
              </NavItem>
              <NavItem>
                <Link className='nav-link' to='/scheduler'>Scheduler</Link>
              </NavItem>
              <NavItem>
                <Link className='nav-link' to='/stages'>Stages</Link>
              </NavItem>
            </Nav>

            {navbar}
          </Collapse>
        </Navbar>

        <div className='page-container-body flex-grow-1 h-100'>
          {body}
        </div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ collapsed: !prevState.collapsed }));
  }
}

export default PageContainer;
