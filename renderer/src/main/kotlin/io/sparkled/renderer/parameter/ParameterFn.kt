package io.sparkled.renderer.parameter

import io.sparkled.model.animation.Colors
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamType
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEasing
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.api.SparkledFill
import java.awt.Color
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ParameterFn<T>(
    val type: ParamType,
    val displayName: String,
    val defaultValue: T,
    val get: (ctx: RenderContext) -> T,
)

class BooleanParameter(
    override val displayName: String,
    override val defaultValue: Boolean,
) : TypedParameter<Boolean>(ParamType.BOOLEAN, Boolean::class)

class ColorParameter(
    override val displayName: String,
    override val defaultValue: Color,
) : TypedParameter<Color>(ParamType.COLOR, Color::class)

class ColorsParameter(
    override val displayName: String,
    override val defaultValue: Colors,
) : TypedParameter<Colors>(ParamType.COLORS, Colors::class)

class DecimalParameter(
    override val displayName: String,
    override val defaultValue: Float,
) : TypedParameter<Float>(ParamType.DECIMAL, Float::class)

class IntParameter(
    override val displayName: String,
    override val defaultValue: Int,
) : TypedParameter<Int>(ParamType.INTEGER, Int::class)

class StringParameter(
    override val displayName: String,
    override val defaultValue: String,
) : TypedParameter<String>(ParamType.STRING, String::class)

abstract class TypedParameter<T : Any>(
    private val paramType: ParamType,
    private val paramClass: KClass<out T>,
) {
    abstract val displayName: String
    abstract val defaultValue: T

    operator fun getValue(thisRef: Any, property: KProperty<*>): ParameterFn<T> {
        return ParameterFn(
            type = paramType,
            displayName = displayName,
            defaultValue = defaultValue,
        ) { ctx ->
            val target: HasArguments = when (thisRef) {
                is SparkledEffect<*> -> ctx.effect
                is SparkledFill -> ctx.effect.fill
                is SparkledEasing -> ctx.effect.easing
                else -> throw RuntimeException("Invalid parameter target ${thisRef.javaClass.name}.")
            }

            ctx.getParam(
                target,
                property.name,
                paramClass,
            ) ?: defaultValue
        }
    }
}
