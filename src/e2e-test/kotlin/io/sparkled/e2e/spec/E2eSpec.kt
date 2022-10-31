package io.sparkled.e2e.spec

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpRequest
import io.sparkled.e2e.util.E2eContext

/**
 * A base class that provides common functionality for E2E tests.
 */
open class E2eSpec : StringSpec() {

    val caches by lazy { E2eContext.caches }
    val db by lazy { E2eContext.db }
    val http by lazy { E2eContext.http }
    inline fun <reified T> inject(): T = E2eContext.inject()

    override suspend fun beforeSpec(spec: Spec) {
        E2eContext.begin()
    }

    override suspend fun afterSpec(spec: Spec) {
        E2eContext.end()
    }

    fun get(url: String, authUser: String? = "user1"): MutableHttpRequest<*> {
        val request: MutableHttpRequest<*> = HttpRequest.GET<Any>(url)
        if (authUser != null) {
            request.basicAuth(authUser, "password")
        }

        return request
    }
}
