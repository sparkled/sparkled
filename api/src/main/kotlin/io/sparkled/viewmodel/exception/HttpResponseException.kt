package io.sparkled.viewmodel.exception

import io.sparkled.api.util.errorResponse
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.error.ErrorViewModel
import io.micronaut.http.HttpResponse

/**
 * This exception can be thrown to pass an HTTP error response up to an API controller method.
 */
class HttpResponseException(val response: HttpResponse<ErrorViewModel>) : RuntimeException() {
    constructor(errorCode: ApiErrorCode) : this(errorResponse(errorCode))
}
