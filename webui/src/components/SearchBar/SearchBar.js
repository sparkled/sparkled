import CircularProgress from '@material-ui/core/CircularProgress';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import InputBase from '@material-ui/core/InputBase';
import Paper from '@material-ui/core/Paper';
import { withStyles } from '@material-ui/core/styles';
import AddIcon from '@material-ui/icons/Add';
import React, { Component } from 'react';

const styles = theme => ({
  searchBar: {
    margin: '0 auto 24px auto',
    padding: '2px 4px',
    display: 'flex',
    alignItems: 'center',
    maxWidth: 400,
  },
  input: {
    marginLeft: 8,
    flex: 1,
  },
  iconButton: {
    padding: 10,
  },
  divider: {
    width: 1,
    height: 28,
    margin: 4,
  }
});

class SongListPage extends Component {

  state = { searchQuery: '' };

  render() {
    const { classes, placeholderText } = this.props;

    return (
      <Paper className={classes.searchBar} elevation={1}>

        <InputBase className={classes.input} placeholder={placeholderText}
                   value={this.state.searchQuery} onChange={this.updateSearchQuery}/>

        <Divider className={classes.divider}/>
        {this.renderAddIcon()}
      </Paper>
    );
  }

  renderAddIcon() {
    const { classes, fetching, onAddButtonClick } = this.props;
    if (fetching) {
      return <CircularProgress className={classes.iconButton} size={44}/>;
    } else {
      return (
        <IconButton color="primary" className={classes.iconButton} onClick={onAddButtonClick}>
          <AddIcon/>
        </IconButton>
      );
    }
  }

  updateSearchQuery = event => {
    const { onSearch } = this.props;

    const searchQuery = event.target.value;
    this.setState({ searchQuery });

    if (onSearch) {
      onSearch(searchQuery);
    }
  }
}

export default withStyles(styles)(SongListPage);
