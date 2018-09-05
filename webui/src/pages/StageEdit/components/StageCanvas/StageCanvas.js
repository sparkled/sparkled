import _ from 'lodash';
import raphael from 'raphael';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { selectStageProp } from '../../actions';
import StageProp from '../StageProp';

class StageCanvas extends Component {

  state = {
    canvasId: `stage-canvas-${+new Date()}`,
    paper: null
  };

  componentDidMount() {
    const { width, height } = this.props.stage;
    const paper = raphael(this.state.canvasId, width, height);

    const deselectRect = paper.rect(0, 0, paper.width, paper.height).attr({ fill: 'rgba(0, 0, 0, 0)', stroke: 'none' });
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
    const { editable, stage } = this.props;

    if (!paper) {
      return [];
    }

    return _.map(stage.stageProps, stageProp => (
      <StageProp key={stageProp.uuid} stageProp={stageProp} paper={paper} editable={editable}/>
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

function mapStateToProps() {
  return {};
}

export default connect(mapStateToProps, { selectStageProp })(StageCanvas);
