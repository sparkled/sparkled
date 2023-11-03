package io.sparkled.e2e.spec

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpRequest
import io.sparkled.e2e.util.E2eContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A base class that provides common functionality for E2E tests.
 */
open class E2eSpec : StringSpec() {

    val caches by lazy { E2eContext.caches }
    val db by lazy { E2eContext.db }
    val http by lazy { E2eContext.http }
    inline fun <reified T> inject(): T = E2eContext.inject()

    /**
     * Jetbrains issues IDE warnings for blocking operations that occur within Kotest specs. This method can be used to
     * offload blocking calls (e.g. database queries) to the IO thread pool.
     */
    suspend fun <T> io(op: () -> T): T {
        return withContext(Dispatchers.IO) {
            op()
        }
    }

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
