import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import StagePropForm from '../StagePropForm';
import { selectStageProp } from '../../actions';

class StagePropList extends Component {

  render() {
    const { className = '', stageProps } = this.props;
    const stagePropEntries = _.map(stageProps, this.createStagePropEntry.bind(this));

    return (
      <div className={"stage-prop-list " + className}>
        {stagePropEntries}
      </div>
    );
  }

  createStagePropEntry(stageProp) {
    const uuid = stageProp.uuid;

    return (
      <StagePropForm key={uuid} stageProp={stageProp} form={`stageProp_${stageProp.uuid}`} initialValues={stageProp}/>
    );
  }
}

function mapStateToProps({ page: { stageEdit } }) {
  return {
    stageProps: stageEdit.present.stage.stageProps
  };
}

export default connect(mapStateToProps, { selectStageProp })(StagePropList);
