package io.sparkled.renderer

import io.kotest.core.spec.style.StringSpec
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.model.util.testStageProp
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.effect.line.LineEffect
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils.render

class GroupedStagePropRenderTest : StringSpec() {

    init {
        "can render stage prop group" {
            val stageProps = listOf(
                testStageProp.copy(groupDisplayOrder = 0, ledCount = 1),
                testStageProp.copy(groupDisplayOrder = 2, ledCount = 4),
                testStageProp.copy(groupDisplayOrder = 1, ledCount = 5),
            )

            val effects = mapOf(
                stageProps[0].id to listOf(lineEffect("#ff0000")),
                stageProps[1].id to listOf(lineEffect("#00ff00")),
                stageProps[2].id to listOf(lineEffect("#0000ff")),
            )

            val renderedStagePropData = render(effects, 11, stageProps)
            println(renderedStagePropData)
            // TODO assert results
        }
    }

    private fun lineEffect(lineColor: String): Effect {
        return Effect(
            endFrame = 10,
            type = LineEffect.id,
            args = mapOf(
                arg("lineLength", "1")
            ),
            easing = Easing(LinearEasing.id),
            fill = Fill(
                SingleColorFill.id,
                BlendMode.NORMAL,
                mapOf(
                    arg("color", lineColor)
                )
            )
        )
    }
}
