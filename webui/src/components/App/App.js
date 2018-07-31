import React, { Fragment } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/scale.css';
import SchedulerPage from '../../pages/Scheduler';
import SongEditPage from '../../pages/SongEdit';
import SongListPage from '../../pages/SongList';
import StageEditPage from '../../pages/StageEdit';
import StageListPage from '../../pages/StageList';
import store from '../../store/store';
import 'bootswatch/dist/superhero/bootstrap.css';
import './App.css';

const RedirectInvalidUrlToIndex = () => <Redirect to='/songs'/>;

const App = () => (
  <Fragment>
    <Provider store={store}>
      <Router>
        <Switch>
          <Route exact path='/songs' component={SongListPage}/>
          <Route exact path='/songs/:songId' component={SongEditPage}/>
          <Route exact path='/stages' component={StageListPage}/>
          <Route exact path='/stages/:stageId' component={StageEditPage}/>
          <Route exact path='/scheduler' component={SchedulerPage}/>
          <Route component={RedirectInvalidUrlToIndex}/>
        </Switch>
      </Router>
    </Provider>
    <Alert position='bottom-right' effect='scale' stack={{ limit: 3 }}/>
  </Fragment>
);

export default App;
