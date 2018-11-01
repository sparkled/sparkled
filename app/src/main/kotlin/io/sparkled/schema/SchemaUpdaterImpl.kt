package io.sparkled.schema

import io.sparkled.persistence.transaction.Transaction
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.exception.LiquibaseException
import liquibase.resource.ClassLoaderResourceAccessor
import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Provider
import javax.persistence.EntityManager

/**
 * Performs an automated database schema upgrade.
 */
open class SchemaUpdaterImpl
@Inject constructor(
    private val entityManagerProvider: Provider<EntityManager>,
    private val transaction: Transaction
) : SchemaUpdater {

    @Throws(Exception::class)
    override fun update() {
        transaction.of {
            val jdbcConnection = jdbcConnection
            val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection)
            val liquibase = Liquibase(CHANGELOG_FILE, ClassLoaderResourceAccessor(), database)

            try {
                liquibase.update("")
            } catch (e: LiquibaseException) {
                logger.error("Failed to update the liquibase schema.", e)
                throw e
            }
        }
    }

    private val jdbcConnection: JdbcConnection
        get() {
            val session = entityManagerProvider.get().unwrap(Session::class.java) as SessionImpl
            val connection = session.connection()
            return JdbcConnection(connection)
        }

    companion object {
        private const val CHANGELOG_FILE = "db.changelog.xml"
        private val logger = LoggerFactory.getLogger(SchemaUpdaterImpl::class.java)
    }
}
