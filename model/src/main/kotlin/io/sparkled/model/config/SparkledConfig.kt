package io.sparkled.model.config

import io.micronaut.context.annotation.Value
import javax.inject.Singleton

@Singleton
data class SparkledConfig(

    /**
     * The HTTPS URL of the Azure Key Vault endpoint used by the API.
     */
    @Value("\${sparkled.directory:.}")
    val directory: String,
)
