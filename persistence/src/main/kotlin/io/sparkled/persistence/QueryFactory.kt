package io.sparkled.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import javax.inject.Provider
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

@Singleton
class QueryFactory(
    val entityManagerFactory: EntityManagerFactory,
    private val entityManagerProvider: Provider<EntityManager>
) : JPAQueryFactory(entityManagerProvider) {

    val entityManager: EntityManager
        get() = entityManagerProvider.get()
}
