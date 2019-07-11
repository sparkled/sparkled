import { AppBar, Divider, Drawer, IconButton, List, ListItem, ListItemIcon, ListItemText, Toolbar } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import SongPageIcon from '@material-ui/icons/Audiotrack';
import BackIcon from '@material-ui/icons/ChevronLeft';
import SequencePageIcon from '@material-ui/icons/FormatPaint';
import StagePageIcon from '@material-ui/icons/Looks';
import MenuIcon from '@material-ui/icons/Menu';
import PlaylistPageIcon from '@material-ui/icons/PlaylistPlay';
import SchedulerPageIcon from '@material-ui/icons/Schedule';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import AppLogo from '../AppLogo';
import BrightnessToggle from '../BrightnessToggle';

const styles = theme => ({
  root: {
    display: 'flex',
  },
  toolbar: {
      display: 'grid',
      gridTemplateColumns: '1fr min-content 1fr',
      gridTemplateRows: '1fr',
      '& > :first-child': {
        justifySelf: 'flex-start'
      },
      '& > :nth-child(2)': {
        justifySelf: 'center',
        [theme.breakpoints.down('sm')]: {
          width: 0
        }
      },
      '& > :last-child': {
        justifySelf: 'flex-end'
      }
  },
  appLogo: {
    height: 30
  },
  toolbarIcon: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
  drawerPaper: {
    width: 240,
  },
  content: {
    display: 'flex',
    flexGrow: 1,
    overflow: 'auto',
    padding: props => theme.spacing(props.spacing === undefined ? 3 : props.spacing)
  }
});

class PageContainer extends Component {

  state = { drawerOpen: false };

  constructor(props) {
    super(props);
    this.mainRef = React.createRef();
  }

  render() {
    const { classes, body, actions } = this.props;

    const drawer = (
      <div>
        <div className={classes.toolbarIcon}>
          <IconButton onClick={this.closeDrawer}>
            <BackIcon/>
          </IconButton>
        </div>
        <Divider/>
        <List>
          <ListItem button variant="default" component={Link} to="/songs">
            <ListItemIcon><SongPageIcon/></ListItemIcon>
            <ListItemText primary="Songs"/>
          </ListItem>

          <ListItem button variant="default" component={Link} to="/stages">
            <ListItemIcon><StagePageIcon/></ListItemIcon>
            <ListItemText primary="Stages"/>
          </ListItem>

          <ListItem button variant="default" component={Link} to="/sequences">
            <ListItemIcon><SequencePageIcon/></ListItemIcon>
            <ListItemText primary="Sequences"/>
          </ListItem>

          <ListItem button variant="default" component={Link} to="/playlists">
            <ListItemIcon><PlaylistPageIcon/></ListItemIcon>
            <ListItemText primary="Playlists"/>
          </ListItem>

          <ListItem button variant="default" component={Link} to="/scheduler">
            <ListItemIcon><SchedulerPageIcon/></ListItemIcon>
            <ListItemText primary="Scheduler"/>
          </ListItem>
        </List>
      </div>
    );

    return (
      <>
        <AppBar position="static">
          <Toolbar className={classes.toolbar}>
            <div>
              <IconButton onClick={this.handleDrawerToggle}>
                <MenuIcon/>
              </IconButton>
            </div>

            <AppLogo className={classes.appLogo}/>

            <div className={classes.toolbarIcon}>
              {actions}
              <BrightnessToggle/>
            </div>
          </Toolbar>
        </AppBar>

        <nav className={classes.drawer}>
          <Drawer
            variant="temporary"
            open={this.state.drawerOpen}
            onClose={this.handleDrawerToggle}
            classes={{ paper: classes.drawerPaper }}>
            {drawer}
          </Drawer>
        </nav>

        <main className={classes.content}>
          {body}
        </main>
      </>
    );
  }

  closeDrawer = () => {
    this.setState({ drawerOpen: false });
  }

  handleDrawerToggle = () => {
    this.setState(state => ({ drawerOpen: !state.drawerOpen }));
  };
}

function mapStateToProps({ page }) {
  const { pageTitle, pageClass } = page.shared;
  return { pageTitle, pageClass };
}

PageContainer = connect(mapStateToProps, {})(PageContainer);
export default withStyles(styles, { withTheme: true })(PageContainer);
