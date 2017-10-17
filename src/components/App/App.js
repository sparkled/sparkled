import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import EditSongPage from '../../pages/EditSong';
import SchedulerPage from '../../pages/Scheduler';
import SongListPage from '../../pages/SongList';
import store from '../../store/store';
import './bootswatch-superhero-v4-alpha.css';

const RedirectInvalidUrlToIndex = () => <Redirect to='/'/>;

const App = () => (
  <Provider store={store}>
    <Router>
      <Switch>
        <Route exact path='/' component={SongListPage}/>
        <Route exact path='/songs/:songId' component={EditSongPage}/>
        <Route exact path='/scheduler' component={SchedulerPage}/>
        <Route component={RedirectInvalidUrlToIndex}/>
      </Switch>
    </Router>
  </Provider>
);

export default App;
