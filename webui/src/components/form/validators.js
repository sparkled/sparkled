import _ from 'lodash';

export const required = value => {
  const isEmpty = value === "" || _.isNil(value);
  return isEmpty ? 'Value is required' : void 0;
};

export const min = minValue => {
  return value => (value < minValue) ? `Value must be at least ${minValue}` : void 0;
};

export const max = maxValue => {
  return value => (value > maxValue) ? `Value must be no greater than ${maxValue}` : void 0;
};
