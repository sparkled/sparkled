package io.sparkled.api.util

import io.sparkled.common.logging.getLogger
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.sparkled.api.util.ApiUtils.logger
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.error.ErrorViewModel


fun errorResponse(
    code: ApiErrorCode,
    devMessage: String? = null,
    err: Throwable? = null
): HttpResponse<ErrorViewModel> {
    val error = ErrorViewModel(code = code, devMessage = devMessage)

    val logParams = arrayOf("id" to error.id, "code" to error.code, "devMessage" to (error.devMessage ?: ""))
    if (err == null) {
        logger.warn(error.userMessage, *logParams)
    } else {
        logger.error(error.userMessage, err, *logParams)
    }

    val httpStatus = try {
        HttpStatus.valueOf(code.httpStatus)
    } catch (e: Exception) {
        logger.error("Failed to convert HTTP status", e, "status" to code.httpStatus)
        HttpStatus.INTERNAL_SERVER_ERROR
    }

    return HttpResponse.status<Any>(httpStatus).body(error)
}

private object ApiUtils {
    val logger = getLogger<ApiUtils>()
}
