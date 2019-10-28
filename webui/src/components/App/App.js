import {CssBaseline} from '@material-ui/core';
import {blue} from '@material-ui/core/colors';
import {createMuiTheme} from '@material-ui/core/styles';
import {ThemeProvider} from '@material-ui/styles';
import {SnackbarProvider} from 'notistack';
import React, {Fragment} from 'react';
import {Provider} from 'react-redux';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-css-effects/scale.css';
import 'react-s-alert/dist/s-alert-default.css';
import PlaylistEditPage from '../../pages/playlistEdit/PlaylistEditPage';
import PlaylistListPage from '../../pages/playlistList/PlaylistListPage';
import SchedulerPage from '../../pages/scheduler/SchedulerPage';
import SequenceEditPage from '../../pages/sequenceEdit/SequenceEditPage';
import SequenceListPage from '../../pages/sequenceList/SequenceListPage';
import SongListPage from '../../pages/songList/SongListPage';
import StageEditPage from '../../pages/stageEdit/StageEditPage';
import StageListPage from '../../pages/stageList/StageListPage';
import store from '../../store/store';
import './App.css';

const theme = createMuiTheme({
  palette: {
    type: 'dark',
    primary: blue
  },
  typography: {
    useNextVariants: true
  }
});

const RedirectInvalidUrlToIndex = () => <Redirect to="/sequences"/>;

const snackbarAnchor = {horizontal: "right", vertical: "bottom"};
const App = () => (
  <Fragment>
    <ThemeProvider theme={theme}>
      <CssBaseline/>
      <SnackbarProvider maxSnack={1} anchorOrigin={snackbarAnchor} TransitionProps={{direction: "up"}}>
        <Provider store={store}>
          <Router>
            <Switch>
              <Route exact path="/songs" component={SongListPage}/>
              <Route exact path="/stages" component={StageListPage}/>
              <Route exact path="/stages/:stageId" component={StageEditPage}/>
              <Route exact path="/sequences" component={SequenceListPage}/>
              <Route exact path="/sequences/:sequenceId" component={SequenceEditPage}/>
              <Route exact path="/playlists" component={PlaylistListPage}/>
              <Route exact path="/playlists/:playlistId" component={PlaylistEditPage}/>
              <Route exact path="/scheduler" component={SchedulerPage}/>
              <Route component={RedirectInvalidUrlToIndex}/>
            </Switch>
          </Router>
        </Provider>
      </SnackbarProvider>
    </ThemeProvider>

    <Alert position="bottom-right" effect="scale" stack={{limit: 3}}/>
  </Fragment>
);

export default App;
