package io.sparkled.model.animation.easing

import com.fasterxml.jackson.annotation.JsonIgnore
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class Easing(
    val type: String,
    val start: Float = 0f,
    val end: Float = 100f,
    override val args: Map<String, List<String>> = emptyMap(),
) : HasArguments {
    @field:JsonIgnore
    override val argsCache = mutableMapOf<String, Any?>()
}
