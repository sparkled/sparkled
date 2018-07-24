import React from 'react';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';

class CustomDateInput extends React.Component {
  render() {
    const { input, disabled, required, label, name, type, meta } = this.props;
    const hasError = meta && meta.touched && meta.error;
    const formGroupClass = hasError ? 'form-group has-danger' : 'form-group';
    const fieldClass = hasError ? 'form-control form-control-danger' : 'form-control';
    const errorContent = hasError ? <div className="form-control-feedback">{meta.error}</div> : null;
    const attrs = { disabled, required };

    return (
      <div className={formGroupClass}>
        <label className="form-control-label" htmlFor={name}>{label} {required ? '*' : ''}</label>
        <input {...input} {...attrs} className={fieldClass} type={type} onClick={this.props.onClick}/>
        {errorContent}
      </div>
    );
  }
}

class DateField extends React.Component {
  state = { startDate: moment() };

  handleChange(date) {
    this.setState({
      startDate: date
    });
  }

  render() {
    return <DatePicker
      customInput={<CustomDateInput />}
      selected={this.state.startDate}
      onChange={this.handleChange.bind(this)}
      dropdownMode="scroll"
      {...this.props}
    />;
  }
}

export default DateField;
