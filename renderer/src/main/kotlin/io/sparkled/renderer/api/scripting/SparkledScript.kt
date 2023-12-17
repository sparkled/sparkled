package io.sparkled.renderer.api.scripting

import io.sparkled.renderer.api.RenderContext

fun interface SparkledScript<T> {
    fun execute(ctx: RenderContext): T
}