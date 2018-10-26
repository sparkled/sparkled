package io.sparkled.persistence.transaction

import javax.inject.Provider
import javax.persistence.EntityManager

class Transaction(private val entityManagerProvider: Provider<EntityManager>) {

    fun <T> of(query: () -> T): T {
        val transaction = entityManagerProvider.get().transaction
        transaction.begin()

        try {
            val result = query.invoke()
            transaction.commit()
            return result
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        }
    }
}
