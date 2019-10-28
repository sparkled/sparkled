import { CircularProgress, Divider, IconButton, InputBase, Paper } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import { Add } from '@material-ui/icons';
import React, { Component } from 'react';

const styles = () => ({
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
          <Add/>
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
