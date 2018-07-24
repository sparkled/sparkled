import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/scale.css';
import EditSongPage from '../../pages/EditSong';
import SchedulerPage from '../../pages/Scheduler';
import SongListPage from '../../pages/SongList';
import StageListPage from '../../pages/StageList';
import store from '../../store/store';
import 'bootswatch/dist/superhero/bootstrap.css';

const RedirectInvalidUrlToIndex = () => <Redirect to='/'/>;

const App = () => (
  <div>
    <Provider store={store}>
      <Router>
        <Switch>
          <Route exact path='/' component={SongListPage}/>
          <Route exact path='/stages' component={StageListPage}/>
          <Route exact path='/songs/:songId' component={EditSongPage}/>
          <Route exact path='/scheduler' component={SchedulerPage}/>
          <Route component={RedirectInvalidUrlToIndex}/>
        </Switch>
      </Router>
    </Provider>
    <Alert position='bottom-right' effect='scale' stack={{ limit: 3 }}/>
  </div>
);

export default App;
