package io.sparkled.renderer.api

interface SparkledEffect<T> : SparkledPlugin {

    fun createState(ctx: RenderContext): T

    fun render(ctx: RenderContext, state: T)
}
