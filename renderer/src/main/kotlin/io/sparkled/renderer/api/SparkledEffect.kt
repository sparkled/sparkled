package io.sparkled.renderer.api

interface SparkledEffect<T> : SparkledPlugin {
    fun createState(ctx: RenderContext): T

    fun render(ctx: RenderContext, state: T)
}

interface StatelessSparkledEffect : SparkledEffect<Unit> {
    fun render(ctx: RenderContext)

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        render(ctx)
    }
}
