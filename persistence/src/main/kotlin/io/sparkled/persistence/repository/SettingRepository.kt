package io.sparkled.persistence.repository

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.sparkled.model.SettingModel

@JdbcRepository(dialect = Dialect.ANSI)
abstract class SettingRepository : CrudRepository<SettingModel, String>
