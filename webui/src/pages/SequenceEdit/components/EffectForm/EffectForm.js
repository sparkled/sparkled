import produce from 'immer';
import _ from 'lodash';
import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { Card, CardBody, CardTitle } from 'reactstrap';
import { Field, FormSection, reduxForm } from 'redux-form';
import * as paramTypes from './param-types';
import ColorPicker from '../../../../components/form/ColorPicker';
import InputField from '../../../../components/form/InputField';
import SingleSelectField from '../../../../components/form/SingleSelectField/SingleSelectField';
import { min, required } from '../../../../components/form/validators';
import trash from '../../../../images/trash.svg';
import { deleteEffect, updateEffect } from '../../actions';
import './EffectForm.css';

const toNumber = value => !value ? null : Number(value);
const minStartFrame = min(0);
const minRepetitions = min(1);
const maxStartFrame = (value, effect) => value > effect.endFrame ? 'Start frame cannot be greater than end frame' : null;
const minEndFrame = (value, effect) => value < effect.startFrame ? 'End frame cannot be less than start frame' : null;
let maxEndFrame = () => null;

class EffectForm extends Component {

  constructor(props) {
    super(props);
    this.updateEffect = this.updateEffect.bind(this);
    this.deleteEffect = this.deleteEffect.bind(this);
    this.renderParamFields = this.renderParamFields.bind(this);
    this.renderParamField = this.renderParamField.bind(this);
  }

  componentWillReceiveProps(newProps) {
    const { initialize, sequence, selectedEffect } = this.props;

    if (selectedEffect !== newProps.selectedEffect) {
      initialize(newProps.selectedEffect);
    }

    if (sequence !== newProps.sequence) {
      const max = sequence.durationFrames - 1;
      maxEndFrame = value => value > max ? 'End frame cannot greater than ' + max : null;
    }
  }

  getEffectUuid(effect) {
    return effect ? effect.uuid : null;
  }

  render() {
    const { className = '', selectedEffect } = this.props;
    const body = !selectedEffect ? this.renderEmpty() : this.renderEffectForm();

    return (
      <Card className={'EffectForm ' + className}>
        <CardBody>
          <CardTitle className="d-flex justify-content-between">
            Effect Properties
            <img alt="Delete" src={trash} style={{ width: 20, height: 20 }}
                 className={selectedEffect ? '' : 'd-none'} onClick={this.deleteEffect}/>
          </CardTitle>
          {body}
        </CardBody>
      </Card>
    );
  }

  renderEmpty() {
    return <div>No effect selected.</div>;
  }

  renderEffectForm() {
    const { sequence, selectedEffect: effect } = this.props;
    return (
      <form>
        <div className="row">
          <Field className="col-6" type="number" parse={toNumber} name="startFrame" component={InputField}
                 label="Start Frame" required={true} validate={[required, minStartFrame, maxStartFrame]}
                 min="0" max={effect.endFrame - 1} onChange={this.updateEffect}/>

          <Field className="col-6" type="number" parse={toNumber} name="endFrame" component={InputField}
                 label="End Frame" required={true} validate={[required, minEndFrame, maxEndFrame]}
                 min={effect.startFrame + 1} max={sequence.durationFrames - 1} onChange={this.updateEffect}/>
        </div>

        <div className="row">
          <Field className="col-6" type="number" parse={toNumber} name="repetitions" component={InputField}
                 label="Repetitions" required={true} validate={[required, minRepetitions]}
                 onChange={this.updateEffect}/>

          <Field className="col-6" type="checkbox" name="reverse" component={InputField} label="Reverse"
                 onChange={this.updateEffect}/>
        </div>

        <hr/>
        {this.renderEffectPropertiesForm()}
        <hr/>
        {this.renderFillPropertiesForm()}
        <hr/>
        {this.renderEasingPropertiesForm()}
      </form>
    );
  }

  renderEffectPropertiesForm() {
    const { selectedEffect, effectTypes } = this.props;
    return (
      <Fragment>
        <h5>Effect Type Properties</h5>
        <Field name="type" component={SingleSelectField} label="Type" allowEmpty={false} required={true}
               validate={required} options={effectTypes} onChange={this.updateEffect}/>
        {this.renderParamFields(selectedEffect.params)}
      </Fragment>
    );
  }

  renderFillPropertiesForm() {
    const { selectedEffect, fillTypes } = this.props;
    return (
      <FormSection name="fill">
        <h5>Fill Properties</h5>
        <Field name="type" component={SingleSelectField} label="Type" allowEmpty={false} required={true}
               validate={required} options={fillTypes} onChange={this.updateEffect}/>
        {this.renderParamFields(selectedEffect.fill.params)}
      </FormSection>
    );
  }

  renderEasingPropertiesForm() {
    const { selectedEffect, easingTypes } = this.props;
    return (
      <FormSection name="easing">
        <h5>Easing Properties</h5>
        <Field name="type" component={SingleSelectField} label="Type" allowEmpty={false} required={true}
               validate={required} options={easingTypes} onChange={this.updateEffect}/>
        {this.renderParamFields(selectedEffect.easing.params)}
      </FormSection>
    );
  }

  renderParamFields(params) {
    return params.map(this.renderParamField);
  }

  renderParamField(param, index) {
    const { easingTypes } = this.props;
    const label = _.startCase(_.toLower(param.name));
    const path = `params.${index}.value.0`;

    if (param.type === paramTypes.COLOR) {
      return (
        <Field key={param.name} name={path} component={ColorPicker} label={label} allowEmpty={false} required={true}
               validate={required} options={easingTypes} onChange={this.updateEffect}/>
      );
    } else {
      return (
        <Field key={param.name} name={path} component={InputField} label={label} allowEmpty={false} required={true}
               validate={required} options={easingTypes} onChange={this.updateEffect}/>
      );
    }
  }

  deleteEffect() {
    const { selectedChannel, deleteEffect, selectedEffect } = this.props;
    deleteEffect(selectedChannel, selectedEffect);
  }

  updateEffect(event, newValue, previousValue, name) {
    const { effectTypes, fillTypes, easingTypes } = this.props;

    // Timeout is used to allow form to finish validation so we have the correct "invalid" value in props.
    setTimeout(() => {
      const { selectedChannel, updateEffect, selectedEffect, invalid } = this.props;
      const updatedEffect = produce(selectedEffect, draft => {
        draft.invalid = invalid;

        // Convert a name path like "fill.params.1.value.0" to an array for _.set() and assign a value to that path.
        _.set(draft, name.split('.'), newValue);

        if (name === 'type') {
          const newType = _.find(effectTypes, { code: newValue});
          draft.params = _.cloneDeep(newType.params);
        } else if (name === 'fill.type') {
          const newFill = _.find(fillTypes, { code: newValue});
          draft.fill.params = _.cloneDeep(newFill.params);
        } else if (name === 'easing.type') {
          const newEasing = _.find(easingTypes, { code: newValue});
          draft.easing.params = _.cloneDeep(newEasing.params);
        }
      });

      updateEffect(selectedChannel, updatedEffect);
    });
  }
}

function mapStateToProps({ page }) {
  const { effectTypes, fillTypes, easingTypes, selectedChannel, selectedEffect, sequence } = page.sequenceEdit.present;
  return { effectTypes, fillTypes, easingTypes, selectedChannel, selectedEffect, sequence };
}

EffectForm = connect(mapStateToProps, { updateEffect, deleteEffect })(EffectForm);
export default reduxForm({ form: 'effect' })(EffectForm);
