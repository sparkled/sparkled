import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Collapse, Navbar, NavbarToggler, Nav, NavItem } from 'reactstrap';
import AppLogo from '../AppLogo';
import './PageContainer.css';

class PageContainer extends Component {

  state = { collapsed: false };

  componentWillReceiveProps(nextProps) {
    const { pageTitle, pageClass = '' } = nextProps;
    document.title = pageTitle ? `Sparkled | ${pageTitle}` : 'Sparkled';

    document.body.classList.remove(this.props.pageClass);
    document.body.classList.add(pageClass);
  }

  render() {
    const { className = '', body, navbar } = this.props;

    return (
      <div className={className + ' page-container d-flex flex-column h-100'}>
        <Navbar className="navbar-expand-lg navbar-dark bg-dark flex-grow-0 flex-shrink-0">
          <Link to="/" className="navbar-brand mr-3">
            <AppLogo/>
          </Link>

          <NavbarToggler onClick={this.toggle.bind(this)} className="navbar-toggler-right"/>

          <Collapse isOpen={this.state.collapsed} navbar>
            <Nav className="mr-auto" navbar>
              <NavItem>
                <Link className={'nav-link ' + this.getPageClass('stage')} to="/stages">Stages</Link>
              </NavItem>
              <NavItem>
                <Link className={'nav-link ' + this.getPageClass('sequence')} to="/sequences">Sequences</Link>
              </NavItem>
              <NavItem>
                <Link className={'nav-link ' + this.getPageClass('playlist')} to="/playlists">Playlists</Link>
              </NavItem>
            </Nav>

            {navbar}
          </Collapse>
        </Navbar>

        <div className="page-container-body flex-grow-1 h-100">
          {body}
        </div>
      </div>
    );
  }

  getPageClass(pageName) {
    const { pageClass = '' } = this.props;
    return pageClass.startsWith(pageName) ? 'active' : '';
  }

  toggle() {
    this.setState(prevState => ({ collapsed: !prevState.collapsed }));
  }
}

function mapStateToProps({ page }) {
  const { pageTitle, pageClass } = page.shared;
  return { pageTitle, pageClass };
}

export default connect(mapStateToProps, { })(PageContainer);
