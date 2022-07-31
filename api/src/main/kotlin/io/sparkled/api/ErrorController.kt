package io.sparkled.api

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.sparkled.model.validator.exception.EntityValidationException
import io.sparkled.api.response.UserFriendlyErrorResponse
import io.sparkled.viewmodel.exception.ViewModelConversionException
import org.slf4j.LoggerFactory

@ExecuteOn(TaskExecutors.IO)
@Controller
class ErrorController {

    @Error(global = true, exception = Throwable::class)
    fun handleInternalServerError(request: HttpRequest<Any>, t: Throwable): HttpResponse<Any> {
        return if (t is EntityValidationException || t is ViewModelConversionException) {
            HttpResponse.badRequest(UserFriendlyErrorResponse(t.message!!))
        } else {
            logger.error("Unexpected error response for URI '${request.path}':", t)
            // TODO return object response instead of string. Do this for all errors.
            HttpResponse.serverError("An unexpected error occurred.")
        }
    }

    @Error(global = true, status = HttpStatus.NOT_FOUND)
    fun handleNotFoundError(request: HttpRequest<Any>): HttpResponse<out Any> {
        return if (!request.path.startsWith("/api")) {
            return rerouteUiRequest()
        } else {
            HttpResponse.notFound()
        }
    }

    /**
     * Redirect nonexistent paths to index.html to support single page app refreshes. For example,
     * if the user navigates to the /editor page and refreshes, they will get a 404. Redirecting to index.html
     * means that the SPA will load up and automatically navigate the user to the correct SPA page based on the URL.
     */
    private fun rerouteUiRequest(): HttpResponse<out Any> {
        val indexPage = ErrorController::class.java.getResource("/webui/index.html")?.readText() ?: ""

        return if (indexPage.isNotEmpty()) {
            HttpResponse.ok(indexPage).contentType(MediaType.TEXT_HTML)
        } else {
            logger.error("Failed to find index.html.")
            HttpResponse.notFound()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ErrorController::class.java)
    }
}
