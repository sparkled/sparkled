package io.sparkled.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

public class QueryFactory extends JPAQueryFactory {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public QueryFactory(Provider<EntityManager> entityManagerProvider) {
        super(entityManagerProvider);
        this.entityManagerProvider = entityManagerProvider;
    }

    public EntityManager getEntityManager() {
        return entityManagerProvider.get();
    }
}
