package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.util.FillUtils
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Based on https://github.com/FastLED/FastLED/blob/master/examples/Fire2012/Fire2012.ino.
 */
object FireEffect : SparkledEffect<MutableList<Int>> {

    enum class Params { COOLING, SPARKING, RANDOM_SEED, SPARKS_START, SPARKS_MIDDLE, SPARKS_END }

    override fun createState(ctx: RenderContext): MutableList<Int> {
        return (0 until ctx.pixelCount)
            .map { 0 }
            .toMutableList()
    }

    override val id = "@sparkled/fire"
    override val name = "Fire"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.int(Params.COOLING.name, "Cooling (%)", 20),
        Param.int(Params.SPARKING.name, "Sparking (%)", 50),
        Param.int(Params.RANDOM_SEED.name, "Random Seed", 1),
        Param.boolean(Params.SPARKS_START.name, "Sparks at start", true),
        Param.boolean(Params.SPARKS_MIDDLE.name, "Sparks at middle", false),
        Param.boolean(Params.SPARKS_END.name, "Sparks at end", false),
    )

    override fun render(ctx: RenderContext, state: MutableList<Int>) {
        val randomSeed = ctx.getParam(ctx.effect, Params.RANDOM_SEED, Int::class, 1)
        val random = Random(ctx.frame.frameIndex + randomSeed)

        val sparksStart = ctx.getParam(ctx.effect, Params.SPARKS_START, Boolean::class, true)
        val sparksMiddle = ctx.getParam(ctx.effect, Params.SPARKS_MIDDLE, Boolean::class, false)
        val sparksEnd = ctx.getParam(ctx.effect, Params.SPARKS_END, Boolean::class, false)

        val rawCooling = ctx.getParam(ctx.effect, Params.COOLING, Int::class, 20)
        val rawSparking = ctx.getParam(ctx.effect, Params.SPARKING, Int::class, 50)
        val cooling = (rawCooling / 100f * 255f).toInt()
        val sparking = (rawSparking / 100f * 255f).toInt()

        // Step 1. Cool down every cell a little
        state.forEachIndexed { i, it ->
            state[i] = max(0, it - random.nextInt((cooling * 10 / ctx.pixelCount) + 2))
        }

        // Step 2. Heat from each cell drifts 'up' and diffuses a little
        for (i in ctx.pixelCount - 1 downTo 2) {
            state[i] = (state[i - 1] + state[i - 2] + state[i - 2]) / 3
        }

        // Step 3. Randomly ignite new 'sparks' of heat
        if (sparksStart) {
            if (random.nextInt(256) < sparking) {
                val i = random.nextInt(min(7, state.lastIndex))
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }
        if (sparksMiddle) {
            if (random.nextInt(256) < sparking) {
                val i = state.size - random.nextInt(min(7, state.lastIndex)) - 1
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }
        if (sparksEnd) {
            if (random.nextInt(256) < sparking) {
                val i = state.size - random.nextInt(min(7, state.lastIndex)) - 1
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }

        // Step 4. Map from heat cells to LED colors
        state.forEachIndexed { i, it ->
            FillUtils.fill(ctx, i, 1f, heatColor(it))
        }
    }

    private fun heatColor(temperature: Int): Color {
        return when {
            temperature > 168 -> Color(255, 255, temperature)
            temperature > 84 -> Color(255, temperature, 0)
            else -> Color(temperature, 0, 0)
        }
    }
}
