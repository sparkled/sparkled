package io.sparkled.persistence.repository

import io.micronaut.data.repository.CrudRepository

fun <E, ID> CrudRepository<E, ID>.findByIdOrNull(id: ID): E? {
    return findById(id).orElse(null)
}
