package io.sparkled.renderer.effect

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.easing.EasingFunctions

abstract class EffectRenderer {

    fun render(sequence: Sequence, channel: RenderedStagePropData, frame: RenderedFrame, stageProp: StageProp, effect: Effect) {
        val progress = getProgress(frame, effect)
        val ctx = RenderContext(sequence, channel, frame, stageProp, effect, progress)
        render(ctx)
    }

    private fun getProgress(frame: RenderedFrame, effect: Effect): Float {
        val easingFunction = EasingFunctions[effect.easing.type]

        val currentFrame = frame.frameNumber - effect.startFrame
        val startFrame = effect.startFrame
        val duration = effect.endFrame - startFrame + 1

        val progress = easingFunction.getProgress(effect.easing, currentFrame, duration)
        if (progress < 0 || progress > 1) {
            throw IllegalStateException("Animation progress is out of bounds: $progress")
        }

        return progress
    }

    protected abstract fun render(ctx: RenderContext)
}