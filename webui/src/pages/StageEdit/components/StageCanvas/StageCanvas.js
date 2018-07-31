import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import raphael from 'raphael';
import StageProp from '../StageProp';
import { selectStageProp } from '../../actions';

class StageCanvas extends Component {

  state = {
    canvasId: `stage-canvas-${+new Date()}`,
    paper: null
  };

  componentDidMount() {
    const { width, height } = this.props.stage;
    const paper = raphael(this.state.canvasId, width, height);

    const deselectRect = paper.rect(0, 0, paper.width, paper.height).attr({fill: 'rgba(0, 0, 0, 0)', stroke: 'none'});
    deselectRect.node.onclick = () => this.props.selectStageProp(null);

    this.setState({ paper });
  }

  render() {
    const { className = '' } = this.props;

    return (
      <div id={this.state.canvasId} className={'stage-canvas ' + className}>
        {this.renderStageProps()}
      </div>
    );
  }

  renderStageProps() {
    const { paper } = this.state;
    const { stageProps } = this.props.stage;

    if (!paper) {
      return [];
    }

    return _.map(stageProps, stageProp => (
      <StageProp key={stageProp.uuid} stageProp={stageProp} paper={paper}/>
    ));
  }

  componentWillUnmount() {
    const { paper } = this.state;

    if (paper) {
      paper.clear();
      paper.remove();
    }
  }
}

function mapStateToProps({ page: { stageEdit } }) {
  return {
    stage: stageEdit.present.stage
  };
}

export default connect(mapStateToProps, { selectStageProp })(StageCanvas);
