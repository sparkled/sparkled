import { SnackbarProvider } from 'notistack'
import React from 'react'
import { Provider } from 'react-redux'
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom'
import Alert from 'react-s-alert'
import { ModalProvider } from 'styled-react-modal'
import 'react-s-alert/dist/s-alert-css-effects/scale.css'
import 'react-s-alert/dist/s-alert-default.css'
import PlaylistEditPage from '../../pages/playlistEdit/PlaylistEditPage'
import SequenceEditPage from '../../pages/sequenceEdit/SequenceEditPage'
import StageEditPage from '../../pages/stageEdit/StageEditPage'
import store from '../../store/store'
import './App.css'
import DashboardScreen from '../../screens/dashboard/DashboardScreen'

const RedirectInvalidUrlToIndex = () => <Redirect to='/dashboard' />

const snackbarAnchor = { horizontal: 'right', vertical: 'bottom' }
const App = () => (
  <>
    <SnackbarProvider maxSnack={1} anchorOrigin={snackbarAnchor} TransitionProps={{ direction: 'up' }}>
      <ModalProvider>
        <Provider store={store}>
          <Router>
            <Switch>
              <Route exact path='/dashboard' component={DashboardScreen} />
              <Route exact path='/stages/:stageId' component={StageEditPage} />
              <Route exact path='/sequences/:sequenceId' component={SequenceEditPage} />
              <Route exact path='/playlists/:playlistId' component={PlaylistEditPage} />
              <Route component={RedirectInvalidUrlToIndex} />
            </Switch>
          </Router>
        </Provider>
      </ModalProvider>
    </SnackbarProvider>

    <Alert position='bottom-right' effect='scale' stack={{ limit: 3 }} />
  </>
)

export default App
