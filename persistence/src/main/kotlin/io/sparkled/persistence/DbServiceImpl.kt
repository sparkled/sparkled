package io.sparkled.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import javax.inject.Singleton

@Singleton
class DbServiceImpl(
    private val jdbi: Jdbi,
    private val objectMapper: ObjectMapper
) : DbService {

    init {
        jdbi.installPlugin(KotlinPlugin())
    }

    override fun init() {
        jdbi.withHandle<Unit, Nothing> { handle ->
            val result = handle.createQuery("SELECT 1 as test").map { rs, _ -> rs.getInt("test") }.first()
            if (result != 1) {
                throw RuntimeException("Failed to connect to database.")
            }
        }
    }

    override fun <T> query(query: DbQuery<T>): T {
        return query.execute(jdbi, objectMapper)
    }
}
