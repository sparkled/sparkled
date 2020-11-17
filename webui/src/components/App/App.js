import { CssBaseline } from '@material-ui/core'
import { blue } from '@material-ui/core/colors'
import { createMuiTheme } from '@material-ui/core/styles'
import { ThemeProvider } from '@material-ui/styles'
import { SnackbarProvider } from 'notistack'
import React, { Fragment } from 'react'
import { Provider } from 'react-redux'
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom'
import Alert from 'react-s-alert'
import 'react-s-alert/dist/s-alert-css-effects/scale.css'
import 'react-s-alert/dist/s-alert-default.css'
import PlaylistEditPage from '../../pages/playlistEdit/PlaylistEditPage'
import SequenceEditPage from '../../pages/sequenceEdit/SequenceEditPage'
import StageEditPage from '../../pages/stageEdit/StageEditPage'
import store from '../../store/store'
import './App.css'
import DashboardScreen from '../../screens/dashboard/DashboardScreen'

const theme = createMuiTheme({
  palette: {
    type: 'dark',
    primary: blue,
  },
  typography: {
    useNextVariants: true,
  },
})

const RedirectInvalidUrlToIndex = () => <Redirect to='/dashboard' />

const snackbarAnchor = { horizontal: 'right', vertical: 'bottom' }
const App = () => (
  <Fragment>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <SnackbarProvider maxSnack={1} anchorOrigin={snackbarAnchor} TransitionProps={{ direction: 'up' }}>
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
      </SnackbarProvider>
    </ThemeProvider>

    <Alert position='bottom-right' effect='scale' stack={{ limit: 3 }} />
  </Fragment>
)

export default App
