package io.sparkled.e2e.api.endpoint.v1.dev

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.micronaut.data.exceptions.DataAccessException
import io.sparkled.e2e.spec.E2eSpec
import io.sparkled.persistence.DbServiceImpl

class DbTransactionE2eTest : E2eSpec() {

    init {
        val dbServiceImpl = inject<DbServiceImpl>()

        beforeEach {
            db.settings.deleteAll()
        }

        "exception in @Transactional method causes DB transaction to roll back" {
            inject<DbServiceImpl>().withTransaction {
                shouldThrow<DataAccessException> {
                    dbServiceImpl.testTransaction(fail = true)
                }

                db.settings.findAll().shouldBeEmpty()
            }
        }

        "successful @Transactional method persists results to DB" {
            inject<DbServiceImpl>().withTransaction {
                dbServiceImpl.testTransaction(fail = false)
                db.settings.findAll().shouldHaveSize(1)
            }
        }
    }
}
