import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Nav } from 'reactstrap';
import LoadingIndicator from '../../components/LoadingIndicator';
import PageContainer from '../../components/PageContainer';
import { setCurrentPage } from '../actions';
import { fetchPlaylists } from './actions';
import './PlaylistEditPage.css';

class StageEditPage extends Component {

  render() {
    const pageBody = (
      <div className="d-flex w-100 h-100">
        {this.renderContent()}
      </div>
    );

    return <PageContainer body={pageBody} navbar={this.renderNavbar()}/>;
  }

  componentWillReceiveProps(nextProps) {
    // const didSave = this.props.saving && !nextProps.saving;
    // if (didSave) {
    //   const saveError = nextProps.saveError;
    //   if (saveError) {
    //     Alert.error(`Save failed: ${saveError}`);
    //   } else {
    //     Alert.success('Stage saved successfully');
    //   }
    // }
  }

  componentDidMount() {
    this.props.setCurrentPage({ pageTitle: 'Edit Playlists', pageClass: 'playlist-edit-page' });
    this.props.fetchPlaylists();
  }

  renderNavbar() {
    return (
      <Nav className="ml-auto" navbar>
      </Nav>
    );
  }

  renderContent() {
    const { fetchError, fetching, playlists } = this.props;

    if (fetching) {
      return this.renderLoading();
    } else if (fetchError) {
      return this.renderEditor();
    } else if (playlists) {
      return this.renderEditor();
    }
  }

  renderLoading() {
    return <LoadingIndicator size={100}/>;
  }

  renderError() {
    return (
      <div className="card border-danger">
        <div className="card-body">
          <p>Failed to load stage: {this.props.fetchError}</p>
          <button className="btn btn-danger" onClick={() => window.location.reload()}>Reload the page</button>
        </div>
      </div>
    );
  }

  renderEditor() {
    const { playlists } = this.props;
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-4">
            <h4>Playlists</h4>
            {_.map(playlists, p => <div key={p.id}>{p.name}</div>)}
          </div>
        </div>
      </div>
    );
  }
}

function mapStateToProps({ page }) {
  const { playlistEdit } = page;
  const { fetching, fetchError, playlists } = playlistEdit;
  return { fetching, fetchError, playlists };
}

export default connect(mapStateToProps, { setCurrentPage, fetchPlaylists })(StageEditPage);
