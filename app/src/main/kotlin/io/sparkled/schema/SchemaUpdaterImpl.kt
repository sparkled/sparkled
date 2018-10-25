package io.sparkled.schema

import io.sparkled.persistence.transaction.Transaction
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import javax.inject.Inject
import javax.inject.Provider
import javax.persistence.EntityManager

/**
 * Performs an automated database schema upgrade.
 */
open class SchemaUpdaterImpl @Inject
constructor(private val entityManagerProvider: Provider<EntityManager>) : SchemaUpdater {

    @Throws(Exception::class)
    override fun update() {
        Transaction(entityManagerProvider).of {
            val jdbcConnection = jdbcConnection
            val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection)
            val liquibase = Liquibase(CHANGELOG_FILE, ClassLoaderResourceAccessor(), database)
            liquibase.update("")
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
    }
}
