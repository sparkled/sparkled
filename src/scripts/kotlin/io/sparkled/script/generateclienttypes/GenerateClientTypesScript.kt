package io.sparkled.script.generateclienttypes

import io.sparkled.common.logging.getLogger
import io.sparkled.model.annotation.GenerateClientType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Modifier
import kotlin.system.exitProcess

/**
 * Generates TypeScript type definitions for all classes annotated with [GenerateClientType].
 */
object GenerateClientTypesScript {

    private val logger = getLogger<GenerateClientTypesScript>()

    @JvmStatic
    fun main(args: Array<String>) {

        try {
            logger.info("Generating types")
            val reflections = Reflections(
                ConfigurationBuilder()
                    .forPackage("io.sparkled")
                    .addClassLoaders(this::class.java.classLoader)
                    .setScanners(Scanners.TypesAnnotated),
            )

            val classes = reflections.getTypesAnnotatedWith(GenerateClientType::class.java)
                .filter { Modifier.isPublic(it.modifiers) }

            val types = ClientTypeMetadataGatherer(classes, jacksonObjectMapper()).gather()

            types.values
                .groupBy { it.path }
                .forEach { ClientTypeCodeGenerator("./webUi/src/types", it.key!!, it.value).generateCodeToFile() }
        } catch (e: Exception) {
            logger.error("Failed to run script", e)
        } finally {
            exitProcess(0)
        }
    }
}
