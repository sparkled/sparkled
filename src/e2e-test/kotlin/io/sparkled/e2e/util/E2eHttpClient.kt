package io.sparkled.e2e.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import java.net.URL

typealias ResponsePair<T> = Pair<HttpResponse<T>, T>

class E2eHttpClient(
    baseUrl: URL,
    private val jsonMapper: ObjectMapper
) {
    val client: BlockingHttpClient = HttpClient.create(baseUrl).toBlocking()


    /** Calls an API endpoint that doesn't return a response body. */
    private inline fun <reified T> getResponseWithoutBody(req: MutableHttpRequest<out Any>): HttpResponse<T> {
        return client.exchange(req, Argument.of(T::class.java))
    }

    /** Calls an API endpoint that returns a JSON object response body. */
    inline fun <reified T> getResponseAndObject(req: MutableHttpRequest<out Any>): Pair<HttpResponse<T>, T> {
        val response = client.exchange(req, Argument.of(T::class.java))
        return response to response.body.get()
    }

    /** Calls an API endpoint that returns a JSON list/array response body. */
    private inline fun <reified T> getResponseAndList(req: MutableHttpRequest<out Any>): Pair<HttpResponse<List<T>>, List<T>> {
        @Suppress("UNCHECKED_CAST")
        val response = client.exchange(req, Argument.of(List::class.java, T::class.java) as Argument<List<T>>)
        return response to response.body.get()
    }

    fun getErrorResponse(fn: () -> Unit): Pair<HttpResponse<*>, Any> {
        val exception = shouldThrow<HttpClientResponseException> {
            fn()
        }

        val body = exception.response.getBody(Argument.STRING).get()
        return exception.response to jsonMapper.readValue(body, Any::class.java)
    }
}
