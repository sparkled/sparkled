import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import HomePage from '../../pages/Home';
import EditSongPage from '../../pages/EditSong';
import SchedulerPage from '../../pages/Scheduler';
import SongListPage from '../../pages/SongList';
import store from '../../store/store';
import './bootswatch-superhero-v4-alpha.css';

const App = () => (
  <Provider store={store}>
    <Router>
      <Switch>
        <Route exact path='/' component={HomePage}/>
        <Route exact path='/scheduler' component={SchedulerPage}/>
        <Route exact path='/songs' component={SongListPage}/>
        <Route exact path='/songs/:songId' component={EditSongPage}/>
      </Switch>
    </Router>
  </Provider>
);

export default App;
