package io.sparkled.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;
import java.util.logging.Logger;

/**
 * Provides a convenient, type safe way to perform simple bulk deletions.
 * @param <T> The type of entity class to delete
 */
public class BulkDeleteQuery<T> implements PersistenceQuery<Integer> {

    private static final Logger logger = Logger.getLogger(BulkDeleteQuery.class.getName());

    public static <T> BulkDeleteQuery<T> from(Class<T> from) {
        return new BulkDeleteQuery<>(from);
    }

    private Class<T> from;
    private SingularAttribute<T, ?> where;
    private Object is;

    private BulkDeleteQuery(Class<T> from) {
        this.from = from;
    }

    public BulkDeleteQuery where(SingularAttribute<T, ?> where) {
        this.where = where;
        return this;
    }

    public BulkDeleteQuery is(Object is) {
        this.is = is;
        return this;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        String tableName = from.getSimpleName();
        String sql = "delete from " + tableName + " where " + where.getName() + " = :value";

        Query query = entityManager.createQuery(sql);
        query.setParameter("value", is);

        int deleted = query.executeUpdate();
        logger.info("Deleted " + deleted + " " + tableName + " record(s).");
        return deleted;
    }
}
