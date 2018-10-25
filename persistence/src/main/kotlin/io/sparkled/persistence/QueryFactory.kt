package io.sparkled.persistence

import com.querydsl.jpa.impl.JPAQueryFactory

import javax.inject.Inject
import javax.inject.Provider
import javax.persistence.EntityManager

class QueryFactory @Inject
constructor(private val entityManagerProvider: Provider<EntityManager>) : JPAQueryFactory(entityManagerProvider) {

    val entityManager: EntityManager
        get() = entityManagerProvider.get()
}
