package io.sparkled.schema

import com.google.inject.persist.Transactional
import liquibase.Liquibase
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.hibernate.Session
import org.hibernate.internal.SessionImpl

import javax.inject.Inject
import javax.inject.Provider
import javax.persistence.EntityManager
import java.sql.Connection

/**
 * Performs an automated database schema upgrade.
 */
class SchemaUpdaterImpl @Inject
constructor(private val entityManagerProvider: Provider<EntityManager>) : SchemaUpdater {

    @Transactional
    @Override
    @Throws(Exception::class)
    fun update() {
        val jdbcConnection = jdbcConnection
        val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection)
        val liquibase = Liquibase(CHANGELOG_FILE, ClassLoaderResourceAccessor(), database)
        liquibase.update("")
    }

    private val jdbcConnection: JdbcConnection
        get() {
            val session = entityManagerProvider.get().unwrap(Session::class.java) as SessionImpl
            val connection = session.connection()
            return JdbcConnection(connection)
        }

    companion object {

        private val CHANGELOG_FILE = "db.changelog.xml"
    }
}
