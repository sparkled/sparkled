package io.sparkled.schema;

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

public class SchemaUpdaterImpl implements SchemaUpdater {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public SchemaUpdaterImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public void update() throws Exception {
        JdbcConnection jdbcConnection = getJdbcConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
        Liquibase liquibase = new Liquibase("db.changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }

    private JdbcConnection getJdbcConnection() {
        SessionImpl session = (SessionImpl) entityManagerProvider.get().unwrap(Session.class);
        Connection connection = session.connection();
        return new JdbcConnection(connection);
    }
}
