package io.sparkled.script.generateclienttypes

import io.sparkled.model.annotation.GenerateClientType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType
import java.time.temporal.Temporal
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaType

/**
 * Uses reflection to visit every class annotated with [GenerateClientType] and build up metadata for code generation.
 */
class ClientTypeMetadataGatherer(
    private val classes: List<Class<*>>,
    private val objectMapper: ObjectMapper,
) {
    private val types = linkedMapOf<Class<*>, ClientTypeMetadata>()

    fun gather(): LinkedHashMap<Class<*>, ClientTypeMetadata> {
        classes.forEach { visit(it) }
        return types
    }

    private fun visit(typeClass: Class<*>): ClientTypeMetadata {
        val simpleType = getSimpleType(typeClass)
        val annotation = getAnnotationOrNull(typeClass, GenerateClientType::class)

        return when {
            simpleType != null -> simpleType
            annotation == null -> throw RuntimeException("Class $typeClass is missing an annotation.")
            else -> {
                if (typeClass in types) {
                    types[typeClass]!!
                } else if (typeClass.isEnum) {
                    visitEnum(typeClass, annotation)
                } else {
                    visitClass(typeClass, annotation)
                }
            }
        }
    }

    private fun visitEnum(
        typeClass: Class<*>,
        annotation: GenerateClientType,
    ): ClientTypeMetadata {
        val fields = typeClass.enumConstants.map {
            ClientTypeFieldMetadata(
                type = nativeStringType,
                name = getEnumFieldName(it as Enum<*>),
                nullable = false,
            )
        }

        val metadata = ClientTypeMetadata(
            category = GeneratedTypeCategory.ENUM,
            path = annotation.path,
            name = typeClass.simpleName,
            fields = fields,
        )

        types[typeClass] = metadata
        return metadata
    }

    /**
     * Enums can be annotated with [JsonValue], in which case we need to use the annotated property for the field name.
     * To achieve this, we render out the enum with Jackson and remove the surrounding quotes.
     */
    private fun getEnumFieldName(it: Enum<*>) = objectMapper.writeValueAsString(it).replace("\"", "")

    private fun visitClass(
        typeClass: Class<*>,
        annotation: GenerateClientType,
    ): ClientTypeMetadata {
        val publicProperties = typeClass.kotlin.declaredMemberProperties.filter { it.visibility == KVisibility.PUBLIC }

        val interfaceTypes = typeClass.interfaces.mapNotNull {
            val interfaceAnnotation = getAnnotationOrNull(it, GenerateClientType::class)
            if (interfaceAnnotation == null) {
                null
            } else {
                visitClass(it, interfaceAnnotation)
            }
        }

        val fields = publicProperties.map {
            val fieldType = it.javaField?.type ?: it.returnType.javaType as Class<*>
            val genericType = it.javaField?.genericType ?: Nothing::class.java
            val simpleType = getSimpleType(fieldType)

            val type = when {
                simpleType != null -> simpleType
                Collection::class.java.isAssignableFrom(fieldType) -> {
                    val parameterType = getGenericParameterType(genericType, 0)
                    nativeArrayType(type = parameterType)
                }

                Map::class.java.isAssignableFrom(fieldType) -> {
                    val keyType = getGenericParameterType(genericType, 0)
                    val valueType = getGenericParameterType(genericType, 1)
                    nativeRecordType(keyType = keyType, valueType = valueType)
                }

                fieldType in types -> types[fieldType]!!
                fieldType.isAnnotationPresent(GenerateClientType::class.java) -> visit(fieldType)

                else -> {
                    throw RuntimeException("Failed to process type $fieldType for class $typeClass. Ensure that the class is annotated with @${GenerateClientType::class.java.simpleName}.")
                }
            }

            val jsonPropertyAnnotation = it.javaField?.let { f -> getAnnotationOrNull(f, JsonProperty::class) }
            val jsonName = jsonPropertyAnnotation?.value

            ClientTypeFieldMetadata(
                name = jsonName ?: it.name,
                displayName = if (jsonName == null) null else it.name,
                type = type,
                nullable = it.returnType.isMarkedNullable,
            )
        }

        val metadata = ClientTypeMetadata(
            interfaceTypes = interfaceTypes,
            category = GeneratedTypeCategory.CLASS,
            path = annotation.path,
            name = typeClass.simpleName,
            fields = fields,
        )

        types[typeClass] = metadata
        return metadata
    }

    private fun getSimpleType(type: Class<*>): ClientTypeMetadata? {
        return when {
            areClassesCompatible(type, Boolean::class.java) -> nativeBooleanType
            areClassesCompatible(type, String::class.java) -> nativeStringType
            areClassesCompatible(type, UUID::class.java) -> nativeStringType
            areClassesCompatible(type, Int::class.java) -> nativeNumberType
            areClassesCompatible(type, Short::class.java) -> nativeNumberType
            areClassesCompatible(type, Long::class.java) -> nativeNumberType
            areClassesCompatible(type, Float::class.java) -> nativeNumberType
            areClassesCompatible(type, Double::class.java) -> nativeNumberType
            Number::class.java.isAssignableFrom(type) -> nativeNumberType
            Temporal::class.java.isAssignableFrom(type) -> nativeStringType
            else -> null
        }
    }

    private fun getGenericParameterType(
        genericType: Type,
        parameterIndex: Int,
    ): ClientTypeMetadata {
        return when (genericType) {
            is TypeVariable<*> -> genericParameterType(genericType.name)
            is ParameterizedType -> {
                getGenericParameterType(genericType.actualTypeArguments[parameterIndex], 0)
            }

            is WildcardType -> visit((genericType.upperBounds[0] as Class<*>))
            is Class<*> -> visit(genericType)
            else -> throw RuntimeException("Unable to handle type argument for $genericType: unexpected parameter.")
        }
    }

    private fun areClassesCompatible(a: Class<*>, b: Class<*>): Boolean {
        return when {
            a === b -> true
            a.isAssignableFrom(b) -> true
            b.isAssignableFrom(a) -> true
            a.kotlin.javaObjectType == b -> true
            b.kotlin.javaObjectType == a -> true
            else -> false
        }
    }

    private fun <T : Annotation> getAnnotationOrNull(element: AnnotatedElement, annotationClass: KClass<T>): T? {
        return try {
            element.getAnnotation(annotationClass.java)
        } catch (e: NullPointerException) {
            null
        }
    }
}

data class ClientTypeMetadata(
    val category: GeneratedTypeCategory,
    val path: String? = null,
    val name: String,
    val interfaceTypes: List<ClientTypeMetadata> = emptyList(),
    val genericTypes: List<ClientTypeMetadata> = emptyList(),
    val fields: List<ClientTypeFieldMetadata> = emptyList(),
) {
    val dependencies: List<ClientTypeMetadata>
        get() {
            val fieldTypes = fields.flatMap { it.type.genericTypes + it.type }
            return (genericTypes + interfaceTypes + fieldTypes)
        }
}

data class ClientTypeFieldMetadata(
    val type: ClientTypeMetadata,
    val name: String,
    val displayName: String? = null,
    val nullable: Boolean,
)

enum class GeneratedTypeCategory {
    CLASS, GENERIC_PARAMETER, ENUM, NATIVE,
}

fun genericParameterType(name: String) = ClientTypeMetadata(
    category = GeneratedTypeCategory.GENERIC_PARAMETER,
    name = name,
)

fun nativeArrayType(type: ClientTypeMetadata) = ClientTypeMetadata(
    category = GeneratedTypeCategory.NATIVE,
    name = "${type.name}[]",
    genericTypes = listOf(type),
)

val nativeBooleanType = ClientTypeMetadata(
    category = GeneratedTypeCategory.NATIVE,
    name = "boolean",
)

val nativeNumberType = ClientTypeMetadata(
    category = GeneratedTypeCategory.NATIVE,
    name = "number",
)

fun nativeRecordType(keyType: ClientTypeMetadata, valueType: ClientTypeMetadata) = ClientTypeMetadata(
    category = GeneratedTypeCategory.NATIVE,
    name = "Record<${keyType.name}, ${valueType.name}>",
    genericTypes = listOf(keyType, valueType),
)

val nativeStringType = ClientTypeMetadata(
    category = GeneratedTypeCategory.NATIVE,
    name = "string",
)
