package io.sparkled.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import kotlin.reflect.KClass

inline fun <reified T> getLogger() = SparkledLogger(T::class)

/**
 * A wrapper around SLF4J, with a convenient wrapper around MDC.
 */
class SparkledLogger(nameClass: KClass<*>) {

    private val logger: Logger by lazy { LoggerFactory.getLogger(nameClass.java) }

    fun debug(message: String, vararg params: Pair<String, Any?>) {
        withMdc(params) { logger.debug(message) }
    }

    fun info(message: String, vararg params: Pair<String, Any?>) {
        withMdc(params) { logger.info(message) }
    }

    fun warn(message: String, vararg params: Pair<String, Any?>) {
        withMdc(params) { logger.warn(message) }
    }

    fun error(message: String, vararg params: Pair<String, Any?>) {
        withMdc(params) { logger.error(message) }
    }

    fun error(message: String, t: Throwable, vararg params: Pair<String, Any?>) {
        withMdc(params) { logger.error(message, t) }
    }

    private fun withMdc(params: Array<out Pair<String, Any?>>, fn: () -> Unit) {
        try {
            params.forEach { MDC.put(it.first, it.second.toString()) }
            fn()
        } catch (e: Exception) {
            error("Failed to print log line.", e)
        } finally {
            params.forEach { MDC.remove(it.first) }
        }
    }
}
