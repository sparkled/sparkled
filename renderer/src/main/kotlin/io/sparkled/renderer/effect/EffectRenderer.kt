package io.sparkled.renderer.effect

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.easing.EasingFunctions

abstract class EffectRenderer {

    fun render(sequence: Sequence, channel: RenderedStagePropData, frame: RenderedFrame, effect: Effect) {
        val progress = getProgress(frame, effect)
        val ctx = RenderContext(sequence, channel, frame, effect, progress)
        render(ctx)
    }

    private fun getProgress(frame: RenderedFrame, effect: Effect): Float {
        val easingFunction = EasingFunctions[effect.getEasing()!!.getType()]

        val startFrame = effect.getStartFrame()
        val duration = effect.getEndFrame() - startFrame + 1

        val repetitions = effect.getRepetitions()
        val framesPerRepetition = duration / repetitions
        val repetitionFrameNumber = (frame.frameNumber - startFrame) % (framesPerRepetition + 1)


        val progress = easingFunction.getProgress(effect.getEasing()!!, repetitionFrameNumber, framesPerRepetition)
        if (progress < 0 || progress > 1) {
            throw IllegalStateException("Animation progress is out of bounds: $progress")
        }

        return if (effect.isReverse()) 1 - progress else progress
    }

    protected abstract fun render(ctx: RenderContext)
}