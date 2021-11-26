package io.sparkled.renderer

import io.kotest.core.spec.style.StringSpec
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.effect.line.LineEffect
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils
import java.util.*

class GroupedStagePropRenderTest : StringSpec() {

    init {
        "can render stage prop group" {
            val stageProps = listOf(
                StagePropEntity(
                    uuid = UUID.fromString("00000000-0000-0000-0000-000000000001"),
                    ledCount = 1,
                    groupId = "GRP",
                    groupDisplayOrder = 0
                ),
                StagePropEntity(
                    uuid = UUID.fromString("00000000-0000-0000-0000-000000000003"),
                    ledCount = 4,
                    groupId = "GRP",
                    groupDisplayOrder = 2
                ),
                StagePropEntity(
                    uuid = UUID.fromString("00000000-0000-0000-0000-000000000002"),
                    ledCount = 5,
                    groupId = "GRP",
                    groupDisplayOrder = 1
                ),
            )

            val effects = mapOf(
                stageProps[0].uuid to listOf(lineEffect("#ff0000")),
                stageProps[1].uuid to listOf(lineEffect("#00ff00")),
                stageProps[2].uuid to listOf(lineEffect("#0000ff")),
            )

            val renderedStagePropData = RenderUtils.render(effects, 11, stageProps)
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
