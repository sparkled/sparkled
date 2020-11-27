package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Based on https://github.com/FastLED/FastLED/blob/master/examples/Fire2012/Fire2012.ino.
 */
object FireEffect : SparkledEffect<MutableList<Int>> {

    override fun createState(ctx: RenderContext): MutableList<Int> {
        return (0 until ctx.channel.ledCount)
            .map { 0 }
            .toMutableList()
    }

    override val id = "@sparkled/fire"
    override val name = "Fire"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.int("COOLING", "Cooling (%)", 20),
        Param.int("SPARKING", "Sparking (%)", 50)
    )

    override fun render(ctx: RenderContext, state: MutableList<Int>) {
        val ledCount = ctx.frame.ledCount
        val random = Random(ctx.frame.frameNumber)

        val rawCooling = ParamUtils.getInt(ctx.effect, "COOLING", 20)
        val rawSparking = ParamUtils.getInt(ctx.effect, "SPARKING", 50)
        val cooling = (rawCooling / 100f * 255f).toInt()
        val sparking = (rawSparking / 100f * 255f).toInt()

        // Step 1. Cool down every cell a little
        state.forEachIndexed { i, it ->
            state[i] = max(0, it - random.nextInt((cooling * 10 / ledCount) + 2))
        }

        // Step 2. Heat from each cell drifts 'up' and diffuses a little
        for (i in ledCount - 1 downTo 2) {
            state[i] = (state[i - 1] + state[i - 2] + state[i - 2]) / 3
        }

        // Step 3. Randomly ignite new 'sparks' of heat near the bottom
        if (random.nextInt(256) < sparking) {
            val i = random.nextInt(7)
            state[i] = min(255, state[i] + random.nextInt(160, 255))
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
