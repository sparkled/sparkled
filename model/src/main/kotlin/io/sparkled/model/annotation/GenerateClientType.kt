package io.sparkled.model.annotation

/**
 * Classes using this annotation will have TypeScript types generated when the GenerateClientTypesScript is executed.
 */
annotation class GenerateClientType(val path: String = "viewModels.ts")
