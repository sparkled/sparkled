import { ErrorViewModel } from '../hooks/api/apiTypes'

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

const unknownError: ErrorViewModel = {
  id: '00000000-0000-0000-0000-000000000000',
  code: 'ERR_UNKNOWN',
  userMessage: 'An unknown error occurred.',
}

const serverConnectionError: ErrorViewModel = {
  id: '00000000-0000-0000-0000-000000000000',
  code: 'ERR_NOT_FOUND',
  userMessage: 'Server could not be contacted.',
}

function getApiError(error: any) {
  if (!error) {
    return unknownError
  } else if (isApiError(error)) {
    return error
  }

  const data = error.response?.data
  if (isApiError(data)) {
    return data
  } else if (error.toString().toLowerCase().includes('error: network error')) {
    return serverConnectionError
  } else {
    return unknownError
  }
}

function isApiError(e: ErrorViewModel | any): e is ErrorViewModel {
  return e && (e as ErrorViewModel).code != null && (e as ErrorViewModel).userMessage != null
}

export { getApiError, getErrorMessage }
