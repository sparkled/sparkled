package io.sparkled.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import org.jdbi.v3.core.HandleCallback
import org.jdbi.v3.core.Jdbi

interface DbQuery<T> {
    fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): T

    /**
     * Convenience extension to avoid needing to specify the exception parameter.
     */
    fun <T> Jdbi.perform(callback: HandleCallback<T, Nothing>): T {
        return withHandle(callback)
    }
}
