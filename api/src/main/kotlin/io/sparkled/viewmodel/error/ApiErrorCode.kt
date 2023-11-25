package io.sparkled.viewmodel.error

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
enum class ApiErrorCode(val httpStatus: Int, val userMessage: String) {
    ERR_AUTH_UNAUTHORIZED(401, "You are not authorized to perform this action."),
    ERR_METHOD_NOT_ALLOWED(405, "Method not allowed."),
    ERR_NOT_FOUND(404, "The resource was not found."),
    ERR_REQUEST_CONTENT_LENGTH_EXCEEDED(400, "The request was too large."),
    ERR_REQUEST_INVALID(400, "The request was invalid."),
    ERR_UNKNOWN(500, "An unknown error occurred."),
}
