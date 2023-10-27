package io.sparkled.renderer

import io.kotest.core.spec.style.StringSpec
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.effect.line.LineEffect
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils
import io.sparkled.util.RenderUtils.render
import java.util.*

class GroupedStagePropRenderTest : StringSpec() {

    init {
        "can render stage prop group" {
            val stageProps = listOf(
                StagePropModel(
                    id = "1",
                    stageId = "1",
                    ledCount = 1,
                    groupCode = "GRP",
                    groupDisplayOrder = 0,
                ),
                StagePropModel(
                    id = "3",
                    ledCount = 4,
                    groupCode = "GRP",
                    groupDisplayOrder = 2
                ),
                StagePropModel(
                    id = "2",
                    ledCount = 5,
                    groupCode = "GRP",
                    groupDisplayOrder = 1
                ),
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
                arg(LineEffect.Params.LENGTH.name, "1")
            ),
            easing = Easing(LinearEasing.id),
            fill = Fill(
                SingleColorFill.id,
                BlendMode.NORMAL,
                mapOf(
                    arg(SingleColorFill.Params.COLOR.name, lineColor)
                )
            )
        )
    }
}
