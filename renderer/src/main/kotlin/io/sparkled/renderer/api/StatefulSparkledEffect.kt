package io.sparkled.renderer.api

interface StatefulSparkledEffect<T> : SparkledPlugin {
    fun createState(ctx: RenderContext): T

    fun render(ctx: RenderContext, state: T)
}

interface StatelessSparkledEffect : StatefulSparkledEffect<Unit> {
    fun render(ctx: RenderContext)

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        render(ctx)
    }
}
