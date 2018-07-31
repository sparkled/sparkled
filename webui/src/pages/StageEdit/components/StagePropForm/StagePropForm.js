import React, { Component } from 'react';
import { Card, CardBody, CardTitle, Collapse } from 'reactstrap';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { deleteStageProp, selectStageProp, updateStageProp } from '../../actions';
import InputField from '../../../../components/form/InputField';
import { min, max, required } from '../../../../components/form/validators';
import trash from '../../../../images/trash.svg';

const minLeds = min(0);
const minScale = min(.25);
const maxScale = max(20);
const minRotation = min(-180);
const maxRotation = max(180);

class StagePropForm extends Component {

  componentWillReceiveProps(newProps) {
    const { initialize, stageProp } = this.props;
    if (stageProp !== newProps.stageProp) {
      initialize(newProps.stageProp);
    }
  }

  render() {
    const { className, selectedStagePropUuid, selectStageProp, stageProp } = this.props;

    const toNumber = value => !value ? null : Number(value);

    const deleteStageProp = this.deleteStageProp.bind(this);
    const updateStageProp = this.updateStageProp.bind(this);
    return (
      <Card className={"stage-prop-list-entry " + className}>
        <CardBody>
          <CardTitle onClick={selectStageProp.bind(this, stageProp.uuid)} className="d-flex justify-content-between">
            {stageProp.name}
            <img alt="Delete" src={trash} style={{width: 20, height: 20}} onClick={deleteStageProp}/>
          </CardTitle>
          <Collapse isOpen={selectedStagePropUuid === stageProp.uuid}>
            <form>
              <Field type="text" name="code" component={InputField} label="Code" required={true} validate={required} onChange={updateStageProp}/>
              <Field type="text" name="name" component={InputField} label="Name" required={true} validate={required} onChange={updateStageProp}/>
              <Field type="text" name="type" component={InputField} label="Type" required={true} validate={required} onChange={updateStageProp}/>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="leds" component={InputField} label="Led Count" validate={[minLeds, required]} onChange={updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="positionX" component={InputField} label="Position X" required={true} validate={required} onChange={updateStageProp}/>
                <Field className="col-6" type="number" parse={toNumber} name="positionY" component={InputField} label="Position Y" required={true} validate={required} onChange={updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="scaleX" component={InputField} label="Scale X" required={true} validate={[minScale, maxScale, required]} onChange={updateStageProp}/>
                <Field className="col-6" type="number" parse={toNumber} name="scaleY" component={InputField} label="Scale Y" required={true} validate={[minScale, maxScale, required]} onChange={updateStageProp}/>
              </div>

              <div className="row">
                <Field className="col-6" type="number" parse={toNumber} name="rotation" component={InputField} label="Rotation" required={true} validate={[minRotation, maxRotation, required]} onChange={updateStageProp}/>
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

function mapStateToProps({ page: { stageEdit } }) {
  return {
    selectedStagePropUuid: stageEdit.present.selectedStagePropUuid
  };
}

StagePropForm = connect(mapStateToProps, { deleteStageProp, selectStageProp, updateStageProp })(StagePropForm);
export default reduxForm({ form: 'stageProp' })(StagePropForm);
