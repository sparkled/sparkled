import { AppBar, IconButton, Toolbar } from '@material-ui/core'
import { withStyles } from '@material-ui/core/styles'
import { ChevronLeft as BackIcon } from '@material-ui/icons'
import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Link } from 'react-router-dom'
import AppLogo from './AppLogo'
import BrightnessToggle from './BrightnessToggle'

const styles = theme => ({
  root: {
    display: 'flex',
  },
  toolbar: {
    display: 'grid',
    gridTemplateColumns: '1fr min-content 1fr',
    gridTemplateRows: '1fr',
    '& > :first-child': {
      justifySelf: 'flex-start',
    },
    '& > :nth-child(2)': {
      justifySelf: 'center',
      [theme.breakpoints.down('sm')]: {
        width: 0,
      },
    },
    '& > :last-child': {
      justifySelf: 'flex-end',
    },
  },
  appLogo: {
    height: 30,
  },
  toolbarIcon: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
  content: {
    display: 'flex',
    flexGrow: 1,
    overflow: 'auto',
    position: 'relative',
    padding: props => theme.spacing(props.spacing === undefined ? 3 : props.spacing),
  },
})

class PageContainer extends Component {
  render() {
    const { classes, children, actions } = this.props

    return (
      <>
        <AppBar position='static'>
          <Toolbar className={classes.toolbar}>
            <div>
              <IconButton component={Link} to='/dashboard'>
                <BackIcon />
              </IconButton>
            </div>

            <AppLogo className={classes.appLogo} />

            <div className={classes.toolbarIcon}>
              {actions}
              <BrightnessToggle />
            </div>
          </Toolbar>
        </AppBar>

        <main className={`${classes.content} ${this.props.className || ''}`}>{children}</main>
      </>
    )
  }
}

function mapStateToProps({ page }) {
  const { pageTitle, pageClass } = page.shared
  return { pageTitle, pageClass }
}

PageContainer = connect(mapStateToProps, {})(PageContainer)
export default withStyles(styles, { withTheme: true })(PageContainer)
