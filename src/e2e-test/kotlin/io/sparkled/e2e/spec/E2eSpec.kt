package io.sparkled.e2e.spec

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.sparkled.e2e.util.E2eContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A base class that provides common functionality for E2E tests.
 */
open class E2eSpec : StringSpec() {

    val caches by lazy { E2eContext.caches }
    val db by lazy { E2eContext.db }
    inline fun <reified T> inject(): T = E2eContext.inject()

    override suspend fun beforeSpec(spec: Spec) {
        E2eContext.begin()
    }

    override suspend fun afterSpec(spec: Spec) {
        E2eContext.end()
    }
}
