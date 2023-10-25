package io.sparkled.api

import com.fasterxml.jackson.core.JsonParseException
import io.sparkled.common.logging.getLogger
import io.micronaut.core.convert.exceptions.ConversionErrorException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.exceptions.ContentLengthExceededException
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.web.router.exceptions.UnsatisfiedRouteException
import io.sparkled.api.util.errorResponse
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.error.ErrorViewModel
import io.sparkled.viewmodel.exception.HttpResponseException

@Controller
class ErrorController {

    @Error(status = HttpStatus.NOT_FOUND, global = true)
    fun handle404Error(request: HttpRequest<*>): HttpResponse<out Any> {
        return if (request.path.startsWith("/api")) {
            logger.warn("Not found error, URL: ${request.uri}.")
            return errorResponse(ApiErrorCode.ERR_NOT_FOUND)
        } else {
            rerouteUiRequest()
        }
    }

    @Error(status = HttpStatus.METHOD_NOT_ALLOWED, global = true)
    fun handle405Error(request: HttpRequest<*>): HttpResponse<ErrorViewModel> {
        logger.warn("Method not allowed error, URL: ${request.uri}.")
        return errorResponse(ApiErrorCode.ERR_METHOD_NOT_ALLOWED)
    }

    @Error(global = true)
    fun handleError(request: HttpRequest<*>, t: Throwable): HttpResponse<ErrorViewModel> {
        return when (t) {
            is HttpResponseException -> t.response
            is AuthorizationException -> {
                logger.warn("Unauthorized error, URL: ${request.uri}. ${t.message}")
                errorResponse(ApiErrorCode.ERR_AUTH_UNAUTHORIZED)
            }

            is ContentLengthExceededException -> {
                logger.error("Content length exceeded, URL: ${request.uri}.", t)
                errorResponse(ApiErrorCode.ERR_REQUEST_CONTENT_LENGTH_EXCEEDED, err = t)
            }

            is ConversionErrorException -> {
                val devMessage = "Failed to convert ${t.argument.name} parameter. ${t.cause?.message}"
                logger.error("Conversion error, URL: ${request.uri}.", t)
                errorResponse(ApiErrorCode.ERR_REQUEST_INVALID, devMessage = devMessage, err = t)
            }

            is JsonParseException -> {
                logger.error("Invalid JSON, URL: ${request.uri}.", t)
                errorResponse(ApiErrorCode.ERR_REQUEST_INVALID, devMessage = "JSON conversion error", err = t)
            }

            is UnsatisfiedRouteException -> {
                errorResponse(ApiErrorCode.ERR_NOT_FOUND)
            }

            else -> {
                logger.error("Unknown error, URL: ${request.uri}.", t)
                errorResponse(ApiErrorCode.ERR_UNKNOWN, err = t)
            }
        }
    }

    /**
     * Redirect nonexistent paths to index.html to support single page app refreshes. For example,
     * if the user navigates to the /editor page and refreshes, they will get a 404. Redirecting to index.html
     * means that the SPA will load up and automatically navigate the user to the correct SPA page based on the URL.
     */
    private fun rerouteUiRequest(): HttpResponse<out Any> {
        val indexPage = javaClass.getResource("/webui/index.html")?.readText() ?: ""

        return if (indexPage.isNotEmpty()) {
            HttpResponse.ok(indexPage).contentType(MediaType.TEXT_HTML)
        } else {
            logger.error("Failed to find index.html.")
            HttpResponse.notFound()
        }
    }

    companion object {
        private val logger = getLogger<ErrorController>()
    }
}
