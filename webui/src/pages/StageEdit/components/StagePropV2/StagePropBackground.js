import * as PIXI from "pixi.js";
import React, { Component } from 'react';

const padding = 5;
const names = { background: "Background", path: "Path", rotateHandle: "RotateHandle" };

export default class StagePropBackground extends Component {

  state = {};

  componentDidMount() {
    this.buildBackground();
  }

  render = () => <></>

  buildBackground() {
    const { pixiContainer, width, height } = this.props;

    const background = new PIXI.Graphics();
    background.name = names.background;
    background.zIndex = 1;

    background.beginFill(0xFF00FF);
    background.drawRect(-padding, -padding, width + (2 * padding), height + (2 * padding));
    background.endFill();

    background.buttonMode = true;
    background.interactive = true;
    background
      .on('pointerdown', this.onDragStart)
      .on('pointermove', this.onDragMove)
      .on('pointerup', this.onDragEnd)
      .on('pointerupoutside', this.onDragEnd);

    pixiContainer.addChild(background);
  }

  onDragStart = event => {
    const { pixiContainer } = this.props;
    // this.props.selectStageProp(this.props.stageProp.uuid);

    const { x, y } = event.data.getLocalPosition(pixiContainer.parent);
    this.setState({
      dragState: { originX: pixiContainer.x, originY: pixiContainer.y, mouseX: x, mouseY: y }
    });
  }

  onDragMove = event => {
    const { pixiContainer } = this.props;
    const { dragState } = this.state;

    if (dragState) {
      const { originX, originY, mouseX, mouseY } = dragState;
      const { x: relativeParentX, y: relativeParentY } = event.data.getLocalPosition(pixiContainer.parent);

      pixiContainer.x = originX + (relativeParentX - mouseX);
      pixiContainer.y = originY + (relativeParentY - mouseY);
    }
  }

  onDragEnd = event => {
    const { pixiContainer, onMoved } = this.props;
    const { dragState } = this.state;
    const { x, y } = event.data.getLocalPosition(pixiContainer.parent);


    if (dragState) {
      const offsetX = x - dragState.mouseX;
      const offsetY = y - dragState.mouseY;
      this.setState({ dragState: null }, () => onMoved(offsetX, offsetY));
    }
  }
}
