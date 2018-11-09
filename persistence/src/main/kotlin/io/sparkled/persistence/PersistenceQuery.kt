package io.sparkled.persistence

interface PersistenceQuery<out T> {

    fun perform(queryFactory: QueryFactory): T
}
