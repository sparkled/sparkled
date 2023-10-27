package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.StatefulSparkledEffect
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Based on https://github.com/FastLED/FastLED/blob/master/examples/Fire2012/Fire2012.ino.
 */
object FireEffect : StatefulSparkledEffect<MutableList<Int>> {

    enum class Params { COOLING, SPARKING, RANDOM_SEED, SPARKS_START, SPARKS_MIDDLE, SPARKS_END }

    override fun createState(ctx: RenderContext): MutableList<Int> {
        return (0 until ctx.ledCount)
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
        val randomSeed = ParamUtils.getInt(ctx.effect, Params.RANDOM_SEED.name, 1)
        val random = Random(ctx.frame.frameNumber + randomSeed)

        val sparksStart = ParamUtils.getBoolean(ctx.effect, Params.SPARKS_START.name, true)
        val sparksMiddle = ParamUtils.getBoolean(ctx.effect, Params.SPARKS_MIDDLE.name, false)
        val sparksEnd = ParamUtils.getBoolean(ctx.effect, Params.SPARKS_END.name, false)

        val rawCooling = ParamUtils.getInt(ctx.effect, Params.COOLING.name, 20)
        val rawSparking = ParamUtils.getInt(ctx.effect, Params.SPARKING.name, 50)
        val cooling = (rawCooling / 100f * 255f).toInt()
        val sparking = (rawSparking / 100f * 255f).toInt()

        // Step 1. Cool down every cell a little
        state.forEachIndexed { i, it ->
            state[i] = max(0, it - random.nextInt((cooling * 10 / ctx.ledCount) + 2))
        }

        // Step 2. Heat from each cell drifts 'up' and diffuses a little
        for (i in ctx.ledCount - 1 downTo 2) {
            state[i] = (state[i - 1] + state[i - 2] + state[i - 2]) / 3
        }

        // Step 3. Randomly ignite new 'sparks' of heat
        if (sparksStart) {
            if (random.nextInt(256) < sparking) {
                val i = random.nextInt(min(7, state.size - 1))
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }
        if (sparksMiddle) {
            if (random.nextInt(256) < sparking) {
                val i = state.size - random.nextInt(min(7, state.size - 1)) - 1
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }
        if (sparksEnd) {
            if (random.nextInt(256) < sparking) {
                val i = state.size - random.nextInt(min(7, state.size - 1)) - 1
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }

        // Step 4. Map from heat cells to LED colors
        state.forEachIndexed { i, it ->
            FillUtils.fill(ctx, i, 1f, heatColor(it))
        }
    }

    private fun getHeat(state: List<Int>, index: Int): Int {
        return state.getOrElse(index) { 0 }
    }

    private fun heatColor(temperature: Int): Color {
        return when {
            temperature > 168 -> Color(255, 255, temperature)
            temperature > 84 -> Color(255, temperature, 0)
            else -> Color(temperature, 0, 0)
        }
    }
}
