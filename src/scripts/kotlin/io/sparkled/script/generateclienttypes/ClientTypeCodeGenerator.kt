package io.sparkled.script.generateclienttypes

import io.sparkled.common.logging.getLogger
import java.io.File
import java.nio.file.Path
import kotlin.io.path.relativeTo

/**
 * Converts client type metadata into a TypeScript file containing type definitions.
 */
class ClientTypeCodeGenerator(
    outputDirectory: String,
    private val filePath: String,
    private val types: List<ClientTypeMetadata>,
) {
    private val fullPath = "${outputDirectory.removeSuffix("/")}/$filePath"

    fun generateCodeToFile() {
        logger.info("Generating source for file $fullPath.")

        val imports = generateImports()
        val typeDeclarations = generateTypeDeclarations()

        val fileHead = """
            @/***************************************************************************************************
            @ * This file was generated by GenerateClientTypesScript.                                           *
            @ * Don't edit this file manually, as it will be overwritten the next time the script is run.       *
            @ **************************************************************************************************/
        """.trimMargin("@")

        val fileBody = """
        @$imports
        @    
        @$typeDeclarations
        """.trimMargin("@").trim() + "\n"

        val fullFile = "$fileHead\n\n$fileBody"
        val normalizedFile = newlineRegex.replace(fullFile, System.lineSeparator())
        writeFile(fullPath, normalizedFile)
    }

    private fun generateImports(): String {
        val allDependencies = types.flatMap { it.dependencies }.groupBy { it.path }
        val dependencyPaths = types
            .flatMap { it.dependencies }
            .mapNotNull { it.path }
            .filter { it != filePath }
            .toSortedSet()

        return dependencyPaths.joinToString("\n") { path ->
            // Produce a string like { Foo, Bar } for the left-hand side of the import.
            val fileDependencies = allDependencies[path] ?: emptyList()
            val dependencyNames = fileDependencies.map { it.name }.toSortedSet().joinToString(", ")
            val dependencyList = "{ $dependencyNames }"

            val filePathWithoutFile = removeFileFromPath(filePath)
            val pathWithoutFile = removeFileFromPath(path)
            val relativePath = Path.of(pathWithoutFile).relativeTo(Path.of(filePathWithoutFile)).let {
                it.toString().ifEmpty { "." }
            }.replace("\\", "/")

            val fileName = path.split("/").last().removeSuffix(".ts")
            val relativePathWithFile = "$relativePath/$fileName"
            "import $dependencyList from '$relativePathWithFile'"
        }
    }

    private fun removeFileFromPath(path: String) = path.split("/").dropLast(1).joinToString("/")

    private fun generateTypeDeclarations() = types.joinToString("\n\n") {
        when (it.category) {
            GeneratedTypeCategory.ENUM -> generateEnumTypeDeclaration(it)
            GeneratedTypeCategory.CLASS -> generateClassTypeDeclaration(it)
            else -> throw RuntimeException("Cannot generate source for category ${it.category}.")
        }
    }

    private fun generateEnumTypeDeclaration(type: ClientTypeMetadata): String {
        val fields = type.fields.joinToString(",\n") {
            val comment = if (it.displayName == null) "" else "/** ${it.displayName} */"
            "'${it.name}' $comment".trimEnd()
        }

        val valueType = "${type.name}Values"

        return """
            export const $valueType = [
                $fields
            ] as const
            export type ${type.name} = typeof $valueType[number]
        """.trimMargin()
    }

    private fun generateClassTypeDeclaration(type: ClientTypeMetadata): String {
        val genericParameters = type.fields
            .flatMap { f -> f.type.genericTypes }
            .filter { f -> f.category == GeneratedTypeCategory.GENERIC_PARAMETER }
            .map { f -> f.name }
            .let { params ->
                if (params.isEmpty()) {
                    ""
                } else {
                    val joined = params.joinToString(", ")
                    "<$joined>"
                }
            }

        val fields = type.fields.sortedBy { it.name }.joinToString("\n") {
            val escapedName = escapeFieldName(it.name)
            val fieldName = if (it.nullable) "$escapedName?" else escapedName

            val comment = if (it.displayName == null) "" else "/** ${it.displayName} */"
            "  $fieldName: ${it.type.name} $comment".trimEnd()
        }

        val interfaces = if (type.interfaceTypes.isEmpty()) {
            ""
        } else {
            type.interfaceTypes.joinToString(" & ", postfix = " & ") { it.name }
        }

        val typeDeclaration = """$interfaces{
        @$fields
        @}
        """.trimMargin()

        return """export type ${type.name}$genericParameters = $typeDeclaration"""
    }

    /**
     * If field names contain special characters, e.g. @id, wrap the field in single quotes.
     */
    private fun escapeFieldName(name: String) = if (fieldNameRegex.matches(name)) name else "'$name'"

    private fun writeFile(fullPath: String, fileContents: String) {
        val file = File(fullPath)
        file.parentFile.mkdirs()
        file.writeText(fileContents)

        logger.info("Wrote file $fullPath to disk.")
    }

    companion object {
        private val logger = getLogger<ClientTypeCodeGenerator>()

        /**
         * Strings generated in Kotlin code will have \n line separators by default. On Windows, Git converts files to
         * use \r\n line endings when they are checked out. If we keep the line endings as \n, Git will consider the
         * file to have changed purely because of the different line endings.
         */
        private val newlineRegex = Regex("\\r\\n|\\r|\\n")

        private val fieldNameRegex = Regex("^[a-zA-Z0-9_]+$")
    }
}
