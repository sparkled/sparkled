package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.parameter.BooleanParameter
import io.sparkled.renderer.parameter.IntParameter
import io.sparkled.renderer.util.FillUtils
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Based on https://github.com/FastLED/FastLED/blob/master/examples/Fire2012/Fire2012.ino.
 */
object FireEffect : SparkledEffect<MutableList<Int>> {

    override fun createState(ctx: RenderContext) = MutableList(ctx.pixelCount) { 0 }

    override val id = "sparkled:fire:1.0.0"
    override val name = "Fire"
    
    private val cooling by IntParameter(displayName = "Cooling (%)", defaultValue = 20)
    private val sparking by IntParameter(displayName = "Sparking (%)", defaultValue = 50)
    private val randomSeed by IntParameter(displayName = "Random seed", defaultValue = 1)
    private val sparksStart by BooleanParameter(displayName = "Sparks at start", defaultValue = true)
    private val sparksMiddle by BooleanParameter(displayName = "Sparks at middle", defaultValue = false)
    private val sparksEnd by BooleanParameter(displayName = "Sparks at end", defaultValue = false)

    override fun render(ctx: RenderContext, state: MutableList<Int>) {
        val random = Random(ctx.frame.frameIndex + randomSeed.get(ctx))

        val cooling = (cooling.get(ctx) / 100f * 255f).toInt()
        val sparking = (sparking.get(ctx) / 100f * 255f).toInt()

        // Step 1. Cool down every cell a little
        state.forEachIndexed { i, it ->
            state[i] = max(0, it - random.nextInt((cooling * 10 / ctx.pixelCount) + 2))
        }

        // Step 2. Heat from each cell drifts 'up' and diffuses a little
        for (i in ctx.pixelCount - 1 downTo 2) {
            state[i] = (state[i - 1] + state[i - 2] + state[i - 2]) / 3
        }

        // Step 3. Randomly ignite new 'sparks' of heat
        if (sparksStart.get(ctx)) {
            if (random.nextInt(256) < sparking) {
                val i = random.nextInt(min(7, state.lastIndex))
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }
        if (sparksMiddle.get(ctx)) {
            if (random.nextInt(256) < sparking) {
                val i = state.size - random.nextInt(min(7, state.lastIndex)) - 1
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }
        if (sparksEnd.get(ctx)) {
            if (random.nextInt(256) < sparking) {
                val i = state.size - random.nextInt(min(7, state.lastIndex)) - 1
                state[i] = min(255, state[i] + random.nextInt(160, 255))
            }
        }

        // Step 4. Map from heat cells to RGB colors
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
