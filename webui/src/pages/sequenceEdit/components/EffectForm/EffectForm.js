import produce from 'immer'
import _ from 'lodash'
import React, { Component, Fragment } from 'react'
import { connect } from 'react-redux'
import { Card, CardBody, CardTitle } from 'reactstrap'
import { Field, FormSection, reduxForm } from 'redux-form'
import * as paramTypes from './paramTypes'
import ColorPickerField from '../../../../components/form/ColorPickerField'
import MultiColorPickerField from '../../../../components/form/MultiColorPickerField'
import InputField from '../../../../components/form/InputField'
import SingleSelectField from '../../../../components/form/SingleSelectField/SingleSelectField'
import { max, min, required } from '../../../../components/form/validators'
import trash from '../../../../images/trash.svg'
import { deleteEffect, updateEffect } from '../../actions'
import './EffectForm.css'

const toNumber = value => (!value ? null : Number(value))
const minStartFrame = min(0)
const minRepetitions = min(1)
const minRepetitionSpacing = min(0)
const minPercent = min(0)
const maxPercent = max(100)
const maxStartFrame = (value, effect) =>
  value > effect.endFrame ? 'Start frame cannot be greater than end frame' : null
const minEndFrame = (value, effect) => (value < effect.startFrame ? 'End frame cannot be less than start frame' : null)
let maxEndFrame = () => null

class EffectForm extends Component {
  componentWillReceiveProps(nextProps) {
    const { initialize, sequence, selectedEffect } = this.props

    if (selectedEffect !== nextProps.selectedEffect) {
      initialize(nextProps.selectedEffect)
    }

    if (sequence !== nextProps.sequence) {
      const max = sequence.frameCount - 1
      maxEndFrame = value => (value > max ? 'End frame cannot greater than ' + max : null)
    }
  }

  getEffectUuid(effect) {
    return effect ? effect.uuid : null
  }

  render() {
    const { className = '', selectedEffect } = this.props
    const body = !selectedEffect ? this.renderEmpty() : this.renderEffectForm()

    return (
      <Card className={'EffectForm ' + className}>
        <CardBody>
          <CardTitle className='d-flex justify-content-between'>
            Effect Properties
            <img
              alt='Delete'
              src={trash}
              style={{ width: 20, height: 20 }}
              className={selectedEffect ? '' : 'd-none'}
              onClick={this.deleteEffect}
            />
          </CardTitle>
          {body}
        </CardBody>
      </Card>
    )
  }

  renderEmpty() {
    return <div>No effect selected.</div>
  }

  renderEffectForm() {
    const { sequence, selectedEffect: effect } = this.props
    return (
      <form>
        <div className='row'>
          <Field
            className='col-6'
            type='number'
            parse={toNumber}
            name='startFrame'
            component={InputField}
            label='Start Frame'
            required
            validate={[required, minStartFrame, maxStartFrame]}
            min='0'
            max={effect.endFrame - 1}
            onChange={this.updateEffect}
          />

          <Field
            className='col-6'
            type='number'
            parse={toNumber}
            name='endFrame'
            component={InputField}
            label='End Frame'
            required
            validate={[required, minEndFrame, maxEndFrame]}
            min={effect.startFrame + 1}
            max={sequence.frameCount - 1}
            onChange={this.updateEffect}
          />
        </div>

        <div className='row'>
          <Field
            className='col-6'
            type='number'
            parse={toNumber}
            name='repetitions'
            component={InputField}
            label='Repetitions'
            required
            validate={[required, minRepetitions]}
            onChange={this.updateEffect}
          />

          <Field
            className='col-6'
            type='number'
            parse={toNumber}
            name='repetitionSpacing'
            component={InputField}
            label='Rep Spacing'
            required
            validate={[required, minRepetitionSpacing]}
            onChange={this.updateEffect}
          />
        </div>

        <hr />
        {this.renderEffectPropertiesForm()}
        <hr />
        {this.renderFillPropertiesForm()}
        <hr />
        {this.renderEasingPropertiesForm()}
      </form>
    )
  }

  renderEffectPropertiesForm() {
    const { selectedEffect, effectTypes } = this.props
    const effectType = effectTypes[selectedEffect.type]

    return (
      <Fragment>
        <h5>Effect Type Properties</h5>
        <Field
          name='type'
          component={SingleSelectField}
          label='Type'
          allowEmpty={false}
          required
          validate={required}
          options={effectTypes}
          onChange={this.updateEffect}
        />
        {this.renderArgumentFields(selectedEffect, effectType)}
      </Fragment>
    )
  }

  renderFillPropertiesForm() {
    const { selectedEffect, blendModes, fillTypes } = this.props
    const fillType = fillTypes[selectedEffect.fill.type]

    return (
      <FormSection name='fill'>
        <h5>Fill Properties</h5>
        <Field
          name='blendMode'
          component={SingleSelectField}
          label='Blend Mode'
          allowEmpty={false}
          required
          validate={required}
          options={blendModes}
          onChange={this.updateEffect}
        />

        <Field
          name='type'
          component={SingleSelectField}
          label='Type'
          allowEmpty={false}
          required
          validate={required}
          options={fillTypes}
          onChange={this.updateEffect}
        />
        {this.renderArgumentFields(selectedEffect.fill, fillType)}
      </FormSection>
    )
  }

  renderEasingPropertiesForm() {
    const { selectedEffect, easingTypes } = this.props
    const easingType = easingTypes[selectedEffect.easing.type]

    return (
      <FormSection name='easing'>
        <h5>Easing Properties</h5>
        <Field
          name='type'
          component={SingleSelectField}
          label='Type'
          allowEmpty={false}
          required
          validate={required}
          options={easingTypes}
          onChange={this.updateEffect}
        />

        <div className='row'>
          <Field
            className='col-6'
            type='number'
            parse={toNumber}
            name='start'
            component={InputField}
            label='Start (%)'
            required
            validate={[required, minPercent, maxPercent]}
            onChange={this.updateEffect}
          />

          <Field
            className='col-6'
            type='number'
            parse={toNumber}
            name='end'
            component={InputField}
            label='End (%)'
            required
            validate={[required, minPercent, maxPercent]}
            onChange={this.updateEffect}
          />
        </div>
        {this.renderArgumentFields(selectedEffect.easing, easingType)}
      </FormSection>
    )
  }

  renderArgumentFields(argParent, type = {}) {
    return (type.params || []).map(param => {
      const arg = argParent.args[param.code] || {}
      return this.renderArgumentField(arg, param)
    })
  }

  renderArgumentField = (arg, param = {}) => {
    const label = param.displayName
    const path = `args.${param.code}`

    if (param.type === paramTypes.BOOLEAN) {
      return (
        <Field
          key={param.code}
          name={path}
          component={InputField}
          type='checkbox'
          label={label}
          onChange={this.updateEffect}
        />
      )
    } else if (param.type === paramTypes.COLOR) {
      return (
        <Field
          key={param.code}
          name={path + '.0'}
          component={ColorPickerField}
          label={label}
          allowEmpty={false}
          required
          validate={required}
          onChange={this.updateEffect}
        />
      )
    } else if (param.type === paramTypes.COLORS) {
      return (
        <Field
          key={param.code}
          name={path}
          component={MultiColorPickerField}
          label={label}
          allowEmpty={false}
          required
          validate={required}
          onChange={this.updateEffect}
        />
      )
    } else {
      return (
        <Field
          key={param.code}
          name={path + '.0'}
          component={InputField}
          label={label}
          allowEmpty={false}
          required
          validate={required}
          onChange={this.updateEffect}
        />
      )
    }
  }

  deleteEffect = () => {
    const { selectedChannel, deleteEffect, selectedEffect } = this.props
    deleteEffect(selectedChannel, selectedEffect)
  }

  updateEffect = (event, newValue, previousValue, path) => {
    const { effectTypes, fillTypes, easingTypes } = this.props

    // Timeout is used to allow form to finish validation so we have the correct "invalid" value in props.
    setTimeout(() => {
      const { selectedChannel, updateEffect, selectedEffect, invalid } = this.props
      const updatedEffect = produce(selectedEffect, draft => {
        draft.invalid = invalid

        // Convert a name path like "fill.args.argName.0" to an array for _.set() and assign a value to that path.
        _.set(draft, path.split('.'), newValue)

        if (path === 'type') {
          const newType = _.find(effectTypes, { code: newValue })
          draft.args = this.convertParamsToArguments(newType.params)
        } else if (path === 'fill.type') {
          const newFill = _.find(fillTypes, { code: newValue })
          draft.fill.args = this.convertParamsToArguments(newFill.params)
        } else if (path === 'easing.type') {
          const newEasing = _.find(easingTypes, { code: newValue })
          draft.easing.args = this.convertParamsToArguments(newEasing.params)
        }
      })

      updateEffect(selectedChannel, updatedEffect)
    })
  }

  convertParamsToArguments(params) {
    return _(params)
      .keyBy('code')
      .mapValues(param => param.defaultValue)
      .value()
  }
}

function mapStateToProps({ page }) {
  const {
    blendModes,
    effectTypes,
    fillTypes,
    easingTypes,
    selectedChannel,
    selectedEffect,
    sequence,
  } = page.sequenceEdit.present
  return {
    blendModes,
    effectTypes,
    fillTypes,
    easingTypes,
    selectedChannel,
    selectedEffect,
    sequence,
  }
}

EffectForm = connect(mapStateToProps, { updateEffect, deleteEffect })(EffectForm)
export default reduxForm({ form: 'effect' })(EffectForm)
