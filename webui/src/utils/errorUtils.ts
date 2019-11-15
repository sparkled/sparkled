function getErrorMessage(error: any) {
  if (!error) {
    return 'An unexpected error occurred'
  } else if (error.userFriendlyError) {
    return error.userFriendlyError
  } else if (typeof error === 'string') {
    return error
  } else {
    return 'An unexpected error occurred'
  }
}

export { getErrorMessage }
