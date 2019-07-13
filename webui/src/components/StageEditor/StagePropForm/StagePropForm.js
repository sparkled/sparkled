import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Card, CardBody, CardTitle, Collapse } from 'reactstrap';
import { Field, reduxForm } from 'redux-form';
import InputField from '../../form/InputField';
import SingleSelectField from '../../form/SingleSelectField';
import { max, min, required } from '../../form/validators';
import trash from '../../../images/trash.svg';
import { deleteStageProp, selectStageProp, updateStageProp } from '../../actions';
import stagePropTypes from '../../../pages/StageEdit/stagePropTypes';

const toNumber = value => !value ? null : Number(value);
const minLedCount = min(0);
const minScale = min(.25);
const maxScale = max(20);
const minRotation = min(-180);
const maxRotation = max(180);
const minBrightness = min(0);
const maxBrightness = max(100);

class StagePropForm extends Component {

  constructor(props) {
    super(props);
    this.deleteStageProp = this.deleteStageProp.bind(this);
    this.updateStageProp = this.updateStageProp.bind(this);
  }

  componentWillReceiveProps(nextProps) {
    const { initialize, stageProp } = this.props;
    if (stageProp !== nextProps.stageProp) {
      initialize(nextProps.stageProp);
    }
  }

  render() {
    const { className = '', selectedStagePropUuid, selectStageProp, stageProp } = this.props;

    return (
      <Card className={"stage-prop-list-entry " + className}>
        <CardBody>
          <CardTitle onClick={() => selectStageProp(stageProp.uuid)} className="d-flex justify-content-between">
            {stageProp.name}
            <img alt="Delete" src={trash} style={{width: 20, height: 20}} onClick={this.deleteStageProp}/>
          </CardTitle>
          <Collapse isOpen={selectedStagePropUuid === stageProp.uuid}>
            <form>
              <Field type="text" name="code" component={InputField} label="Code" required={true} validate={required} onChange={this.updateStageProp}/>
              <Field type="text" name="name" component={InputField} label="Name" required={true} validate={required} onChange={this.updateStageProp}/>
              <Field type="text" name="type" component={SingleSelectField} options={stagePropTypes} allowEmpty={false} label="Type" required={true} validate={required} onChange={this.updateStageProp}/>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="ledCount" component={InputField} label="Led Count" validate={[minLedCount, required]} onChange={this.updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="checkbox" name="reverse" component={InputField} label="Reverse" onChange={this.updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="positionX" component={InputField} label="Position X" required={true} validate={required} onChange={this.updateStageProp}/>
                <Field className="col-6" type="number" parse={toNumber} name="positionY" component={InputField} label="Position Y" required={true} validate={required} onChange={this.updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="scaleX" component={InputField} label="Scale X" required={true} validate={[minScale, maxScale, required]} onChange={this.updateStageProp}/>
                <Field className="col-6" type="number" parse={toNumber} name="scaleY" component={InputField} label="Scale Y" required={true} validate={[minScale, maxScale, required]} onChange={this.updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="rotation" component={InputField} label="Rotation" required={true} validate={[minRotation, maxRotation, required]} onChange={this.updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="brightness" component={InputField} label="Brightness (%)" required={true} validate={[minBrightness, maxBrightness, required]} onChange={this.updateStageProp}/>
              </div>
            </form>
          </Collapse>
        </CardBody>
      </Card>
    );
  }

  deleteStageProp() {
    const { deleteStageProp, stageProp } = this.props;
    deleteStageProp(stageProp.uuid);
  }

  updateStageProp(event, newValue, previousValue, name) {
    // Timeout is used to allow form to finish validation so we have the correct "invalid" value in props.
    setTimeout(() => {
      const { invalid, stageProp, updateStageProp } = this.props;
      updateStageProp({ ...stageProp, [name]: newValue, invalid });
    });
  }
}

function mapStateToProps({ form, page: { stageEdit } }) {
  return {
    selectedStagePropUuid: stageEdit.present.selectedStagePropUuid
  };
}

StagePropForm = connect(mapStateToProps, { deleteStageProp, selectStageProp, updateStageProp })(StagePropForm);
export default reduxForm({})(StagePropForm);
