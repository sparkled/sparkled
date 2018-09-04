import _ from 'lodash';
import { Component } from 'react';
import { connect } from 'react-redux';
import 'raphael';
import propTypes from '../../propTypes';
import { selectStageProp, updateStageProp } from '../../actions';
import 'Raphael.FreeTransform';

const ledRadius = 5;
const strokeWidth = 4;

class StageProp extends Component {

  state = {
    freeTransform: {},
    leds: []
  };

  componentWillMount() {
    const { paper, stageProp } = this.props;

    const set = this.createStagePropSet(paper, stageProp);
    const ftProperties = this.getFreeTransformProperties(stageProp);

    const updateStageProp = this.updateStageProp.bind(this);
    const freeTransform = paper.freeTransform(set, ftProperties, updateStageProp);
    this.applyTransforms(freeTransform, stageProp);
    this.setState({ freeTransform });
  }

  createStagePropSet(paper, stageProp) {
    const svgPath = propTypes[stageProp.type].path;
    const path = paper.path(svgPath).attr({
      stroke: '#fff',
      'stroke-width': strokeWidth
    });

    const { width, height } = path.getBBox();
    const rect = paper.rect(0, 0, width, height).attr({
      fill: 'rgba(0, 0, 0, 0)',
      stroke: 'none'
    });

    const selectStageProp = () => this.props.selectStageProp(stageProp.uuid);
    rect.node.onclick = selectStageProp;
    path.node.onclick = selectStageProp;

    return paper.set().push(rect).push(path);
  }

  updateStageProp(freeTransform, events) {
    const { stageProp } = this.props;
    const eventName = (events.length > 0) ? events[0] : '';

    this.drawStagePropLeds();

    if (_(eventName).includes(' end')) {
      this.props.updateStageProp({ ...stageProp,
        scaleX: _.ceil(freeTransform.attrs.scale.x, 2),
        scaleY: _.ceil(freeTransform.attrs.scale.y, 2),
        positionX: Math.floor(freeTransform.attrs.translate.x),
        positionY: Math.floor(freeTransform.attrs.translate.y),
        rotation: Math.floor(freeTransform.attrs.rotate)
      });
    }
  }

  getFreeTransformProperties() {
    return {
      draw: ['bbox'],
      snap: { rotate: 5, scale: 5},
      scale: ['bboxCorners', 'bboxSides'],
      keepRatio: ['bboxCorners'],
      range: { scale: [ 25, 99999 ] }
    };
  }

  shouldComponentUpdate(nextProps, nextState) {
    const { freeTransform } = nextState;
    return freeTransform.items.length > 0;
  }

  render() {
    const { freeTransform } = this.state;
    const { selectedStagePropUuid, stageProp } = this.props;

    const { uuid, invalid } = stageProp;
    freeTransform.items[1].el.attr({stroke: invalid ? '#f00' : '#fff'});

    if (invalid) {
      freeTransform.hideHandles();
    } else {
      const isSelected = uuid === selectedStagePropUuid;
      freeTransform.items[0].el.attr({ fill: isSelected ? 'rgba(0, 0, 0, .1)' : 'rgba(0, 0, 0, 0)' });


      if (isSelected) {
        freeTransform.showHandles();
      } else {
        freeTransform.hideHandles();
      }

      this.applyTransforms(freeTransform, stageProp);
      this.drawStagePropLeds();
    }

    return [];
  }

  applyTransforms(freeTransform, stageProp) {
    freeTransform.attrs.translate = { x: stageProp.positionX, y: stageProp.positionY };
    freeTransform.attrs.scale = { x: stageProp.scaleX, y: stageProp.scaleY };
    freeTransform.attrs.rotate = stageProp.rotation;
    freeTransform.apply();
  }

  drawStagePropLeds() {
    const { freeTransform } = this.state;
    const { paper, stageProp } = this.props;

    if (stageProp.leds >= 0) {
      this.removeExcessLeds(stageProp);
      this.addMissingLeds(stageProp);

      const path = freeTransform.items[1].el;
      const transform = paper.canvas.getScreenCTM().inverse().multiply(path.node.getScreenCTM());
      this.updateLedPositions(stageProp, path, transform);
    }
  }

  removeExcessLeds(stageProp) {
    const { leds } = this.state;

    while (leds.length > stageProp.leds) {
      leds.pop().remove();
    }
  }

  addMissingLeds(stageProp) {
    const { leds } = this.state;
    const { paper } = this.props;

    while (leds.length < stageProp.leds) {
      const led = paper.circle(-100, -100, ledRadius).attr({ fill: '#000' });
      leds.push(led);
    }
  }

  updateLedPositions(stageProp, path, transform) {
    const { leds } = this.state;

    for (let i = 0; i < stageProp.leds; i++) {
      const { x, y } = this.getLedPosition(path, transform, i, stageProp.leds);
      leds[i].attr({ cx: x, cy: y });
    }
  }

  getLedPosition(path, transform, index, ledCount) {
    if (this.isOpenShape(path) && ledCount > 1) {
      ledCount--;
    }

    const pathIndex = index / ledCount * path.getTotalLength();
    const { x, y } = path.node.getPointAtLength(pathIndex).matrixTransform(transform);
    return { x, y };
  }

  isOpenShape(path) {
    const { node } = path;
    const { x: x1, y: y1 } = node.getPointAtLength(0);
    const { x: x2, y: y2 } = node.getPointAtLength(path.getTotalLength());

    // A shape is considered "open" if the start and end points aren't in the same place (e.g. an arch vs. a circle)
    return Math.abs(x2 - x1) + Math.abs(y2 - y1) > 0.0001;
  }

  componentWillUnmount() {
    const { freeTransform, leds } = this.state;
    const { items } = freeTransform;

    freeTransform.unplug();
    _.forEach(items, item => item.el.remove());
    _.forEach(leds, led => led.remove());
  }

}

function mapStateToProps({ page: { stageEdit } }) {
  return {
    stage: stageEdit.present.stage,
    selectedStagePropUuid: stageEdit.present.selectedStagePropUuid
  };
}

export default connect(mapStateToProps, { updateStageProp, selectStageProp })(StageProp);
