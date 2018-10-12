package io.sparkled.schema;

import com.google.inject.persist.Transactional;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Performs an automated database schema upgrade.
 */
public class SchemaUpdaterImpl implements SchemaUpdater {

    private static final String CHANGELOG_FILE = "db.changelog.xml";

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public SchemaUpdaterImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Transactional
    @Override
    public void update() throws Exception {
        JdbcConnection jdbcConnection = getJdbcConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
        Liquibase liquibase = new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }

    private JdbcConnection getJdbcConnection() {
        SessionImpl session = (SessionImpl) entityManagerProvider.get().unwrap(Session.class);
        Connection connection = session.connection();
        return new JdbcConnection(connection);
    }
}
