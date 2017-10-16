import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Collapse, Navbar, NavbarToggler, NavbarBrand, Nav, NavItem } from 'reactstrap';
import AppLogo from '../AppLogo';

class PageContainer extends Component {

  state = { collapsed: false };

  render() {
    const { body } = this.props;

    return (
      <div>
        <Navbar className="navbar-toggleable-md bg-primary navbar-inverse">
          <NavbarBrand className="mr-3">
            <AppLogo/>
          </NavbarBrand>

          <NavbarToggler onClick={this.toggle.bind(this)} className="navbar-toggler-right"/>

          <Collapse isOpen={this.state.collapsed} navbar>
            <Nav className="mr-auto" navbar>
              <NavItem>
                <Link className="nav-link" to="/">Home</Link>
              </NavItem>
              <NavItem>
                <Link className="nav-link" to="/scheduler">Scheduler</Link>
              </NavItem>
            </Nav>
          </Collapse>
        </Navbar>

        <div className="container">{body}</div>
      </div>
    );
  }

  toggle() {
    this.setState(prevState => ({ collapsed: !prevState.collapsed }));
  }
}

export default PageContainer;
