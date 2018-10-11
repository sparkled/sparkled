import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import uuidv4 from 'uuid/v4';
import stagePropTypes from '../../stagePropTypes';
import { addStageProp } from '../../actions';

class PropSelector extends Component {

  render() {
    const { className } = this.props;

    const propButtons = _.map(stagePropTypes, (propType, typeCode) => (
      <div key={typeCode} className="prop-selector-item bg-secondary d-flex flex-column align-items-center"
           onClick={this.addStageProp.bind(this, propType, typeCode)}>
        {propType.name}

        <svg width="30px" height="30px" viewBox={`-10 -10  ${propType.width + 20} ${propType.height + 20}`}>
          <path d={propType.path} fill="none" stroke="#fff" strokeWidth="5px"/>
        </svg>
      </div>
    ));

    return (
      <div className={"prop-selector w-100 d-flex flex-row align-items-center justify-content-center " + className}>
        {propButtons}
      </div>
    );
  }

  addStageProp(propType, typeCode) {
    const { addStageProp, stage } = this.props;
    const displayOrder = stage.stageProps.length;

    addStageProp({
      uuid: uuidv4(),
      stageId: stage.id,
      type: typeCode,
      code: `PROP_${displayOrder + 1}`,
      name: `New ${propType.name}`,
      ledCount: 10,
      displayOrder,
      positionX: 0,
      positionY: 0,
      rotation: 0,
      scaleX: 1,
      scaleY: 1
    });
  }
}

function mapStateToProps({ page: { stageEdit } }) {
  return {
    stage: stageEdit.present.stage
  };
}

export default connect(mapStateToProps, { addStageProp })(PropSelector);
