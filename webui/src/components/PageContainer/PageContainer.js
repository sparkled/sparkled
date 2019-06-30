import AppBar from '@material-ui/core/AppBar';
import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { withStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
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
  appLogo: {
    flexGrow: 1,
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
    padding: theme.spacing(3)
  },
});

class PageContainer extends Component {

  state = { drawerOpen: false };

  componentWillReceiveProps(nextProps) {
    const { pageTitle, pageClass = '' } = nextProps;
    document.title = pageTitle ? `Sparkled | ${pageTitle}` : 'Sparkled';

    document.body.classList.remove(this.props.pageClass);
    document.body.classList.add(pageClass);
  }

  render() {
    const { classes, body, navbar } = this.props;

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

          {navbar}
        </List>
      </div>
    );

    return (
      <>
        <AppBar position="static">
          <Toolbar>
            <IconButton onClick={this.handleDrawerToggle}>
              <MenuIcon/>
            </IconButton>

            <AppLogo className={classes.appLogo}/>

            <BrightnessToggle/>
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
