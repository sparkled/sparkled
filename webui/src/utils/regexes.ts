/** A code used by the system to identify an object such as a stage prop. */
export const CODE_NAME = /^[a-zA-Z\d_]+$/

/** One or more numbers. */
export const POSITIVE_INTEGER = /^\d+$/

/** One or more numbers, followed optionally by a single decimal place and one or more numbers. */
export const POSITIVE_NUMBER = /^([.]\d+|\d+(\.\d+)?)$/
