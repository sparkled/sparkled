import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Collapse, Navbar, NavbarToggler, Nav, NavItem } from 'reactstrap';
import AppLogo from '../AppLogo';

class PageContainer extends Component {

  state = { collapsed: false };

  render() {
    const { body, navbar } = this.props;

    return (
      <div>
        <Navbar className="navbar-expand-lg navbar-dark bg-dark">
          <Link to="/" className="navbar-brand mr-3">
            <AppLogo/>
          </Link>

          <NavbarToggler onClick={this.toggle.bind(this)} className="navbar-toggler-right"/>

          <Collapse isOpen={this.state.collapsed} navbar>
            <Nav className="mr-auto" navbar>
              <NavItem>
                <Link className="nav-link" to="/">Home</Link>
              </NavItem>
              <NavItem>
                <Link className="nav-link" to="/scheduler">Scheduler</Link>
              </NavItem>
              <NavItem>
                <Link className="nav-link" to="/stages">Stages</Link>
              </NavItem>
            </Nav>

            {navbar}
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
