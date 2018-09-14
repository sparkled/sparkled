import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Card, CardBody, CardTitle } from 'reactstrap';
import { Field, reduxForm } from 'redux-form';
import InputField from '../../../../components/form/InputField';
import { min, required } from '../../../../components/form/validators';
import trash from '../../../../images/trash.svg';
import { updateEffect } from '../../actions';

const toNumber = value => !value ? null : Number(value);
const minStartFrame = min(0);

const maxStartFrame = (value, effect) => value > effect.endFrame ? 'Start frame cannot be greater than end frame' : null;
const minEndFrame = (value, effect) => value < effect.startFrame ? 'End frame cannot be less than start frame' : null;

class EffectForm extends Component {

  constructor(props) {
    super(props);
    this.updateEffect = this.updateEffect.bind(this);
    this.deleteEffect = this.deleteEffect.bind(this);
  }

  componentWillReceiveProps(newProps) {
    const { initialize, selectedEffect } = this.props;

    if (selectedEffect !== newProps.selectedEffect) {
      initialize(newProps.selectedEffect);
    }
  }

  getEffectUuid(effect) {
    return effect ? effect.uuid : null;
  }

  render() {
    const { className, selectedEffect } = this.props;

    // const toNumber = value => !value ? null : Number(value);

    // const deleteEffect = this.deleteEffect.bind(this);
    // const updateEffect = this.updateEffect.bind(this);

    const body = selectedEffect ? this.renderEffectForm(selectedEffect) : this.renderEmpty();
    return (
      <Card className={"effect-form " + className}>
        <CardBody>
          <CardTitle className="d-flex justify-content-between">
            Effect Properties
            <img alt="Delete" src={trash} style={{ width: 20, height: 20 }} onClick={() => {
            }}/>
          </CardTitle>
          {body}
        </CardBody>
      </Card>
    );
  }

  renderEffectForm(effect) {

    return (
      <form>
        <Field type="text" name="type" component={InputField} label="Type" required={true} validate={required}
               onChange={this.updateEffect}/>

        <div className="row">
          <Field className="col-6" type="number" parse={toNumber} name="startFrame" component={InputField}
                 label="Start Frame" required={true} validate={[required, minStartFrame, maxStartFrame]}
                 onChange={this.updateEffect}/>

          <Field className="col-6" type="number" parse={toNumber} name="endFrame" component={InputField}
                 label="End Frame" required={true} validate={[required, minEndFrame]} onChange={this.updateEffect}/>
        </div>
        {/*<Field type="text" name="code" component={InputField} label="Code" required={true} validate={required} onChange={updateEffect}/>
            <Field type="text" name="name" component={InputField} label="Name" required={true} validate={required} onChange={updateEffect}/>
            <Field type="text" name="type" component={InputField} label="Type" required={true} validate={required} onChange={updateEffect}/>

            <div className="row">
              <Field className="col-6" type="number" parse={toNumber} name="leds" component={InputField} label="Led Count" validate={[minLeds, required]} onChange={updateEffect}/>
            </div>

            <div className="row">
              <Field className="col-6" type="number" parse={toNumber} name="positionX" component={InputField} label="Position X" required={true} validate={required} onChange={updateEffect}/>
              <Field className="col-6" type="number" parse={toNumber} name="positionY" component={InputField} label="Position Y" required={true} validate={required} onChange={updateEffect}/>
            </div>

            <div className="row">
              <Field className="col-6" type="number" parse={toNumber} name="scaleX" component={InputField} label="Scale X" required={true} validate={[minScale, maxScale, required]} onChange={updateEffect}/>
              <Field className="col-6" type="number" parse={toNumber} name="scaleY" component={InputField} label="Scale Y" required={true} validate={[minScale, maxScale, required]} onChange={updateEffect}/>
            </div>

            <div className="row">
              <Field className="col-6" type="number" parse={toNumber} name="rotation" component={InputField} label="Rotation" required={true} validate={[minRotation, maxRotation, required]} onChange={updateEffect}/>
            </div>*/}
      </form>
    );
  }

  renderEmpty() {
    return <div>No effect selected.</div>;
  }

  deleteEffect() {
    const { deleteEffect, effect } = this.props;
    deleteEffect(effect.uuid);
  }

  updateEffect(event, newValue, previousValue, name) {
    // Timeout is used to allow form to finish validation so we have the correct "invalid" value in props.
    setTimeout(() => {
      const { updateEffect, selectedChannel, selectedEffect, invalid } = this.props;
      updateEffect(selectedChannel, { ...selectedEffect, [name]: newValue, invalid });
    });
  }
}

function mapStateToProps({ page }) {
  const { selectedChannel, selectedEffect, sequence } = page.sequenceEdit.present;
  return { selectedChannel, selectedEffect, sequence };
}

EffectForm = connect(mapStateToProps, { updateEffect })(EffectForm);
export default reduxForm({ form: 'effect' })(EffectForm);
