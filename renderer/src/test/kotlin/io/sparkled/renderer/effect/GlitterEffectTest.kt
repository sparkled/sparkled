package io.sparkled.renderer.effect

import io.kotest.core.spec.style.StringSpec
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils

class GlitterEffectTest : StringSpec() {

    init {
        "can render low density" {
            val effect = Effect(
                endFrame = 29,
                type = GlitterEffect.id,
                args = mapOf(
                    arg("density", .1f),
                    arg("lifetimeMs", 200)
                ),
                easing = Easing(LinearEasing.id, 0f, 100f),
                fill = Fill(
                    SingleColorFill.id,
                    BlendMode.NORMAL,
                    mapOf(
                        arg("color", "#ff0000")
                    )
                )
            )

            val renderedStagePropData = RenderUtils.render(effect, effect.endFrame + 1, 10)
// TODO
//            assertThat(
//                renderedStagePropData, hasRenderedFrames(
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x120000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x070000, 0x000000, 0x000000),
//                f(0x000000, 0x1D0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x070000, 0x000000, 0x000000),
//                f(0x000000, 0x200000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x1B0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x0F0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x160000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x420000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x6E0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x920000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0xAB0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0xBC0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x9B0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x6D0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x460000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x280000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x110000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x010000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x070000, 0x000000, 0x000000, 0x000000, 0x070000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x070000, 0x000000, 0x000000, 0x000000, 0x070000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000)
//            )
//            )
        }

        "can render medium density" {
            val effect = Effect(
                endFrame = 29,
                type = GlitterEffect.id,
                args = mapOf(
                    arg("density", .5f),
                    arg("lifetimeMs", 200)
                ),
                easing = Easing(LinearEasing.id, 0f, 100f),
                fill = Fill(
                    SingleColorFill.id,
                    BlendMode.NORMAL,
                    mapOf(
                        arg("color", "#ff0000")
                    )
                )
            )

            val renderedStagePropData = RenderUtils.render(effect, effect.endFrame + 1, 10)
// TODO
//            assertThat(
//                renderedStagePropData, hasRenderedFrames(
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x120000, 0x000000, 0x070000, 0x070000, 0x070000, 0x160000, 0x000000, 0x0B0000, 0x000000),
//                f(0x000000, 0x1D0000, 0x080000, 0x070000, 0x070000, 0x160000, 0x240000, 0x000000, 0x1E0000, 0x000000),
//                f(0x000000, 0x200000, 0x170000, 0x010000, 0x010000, 0x2D0000, 0x2B0000, 0x010000, 0x380000, 0x000000),
//                f(0x000000, 0x1B0000, 0x2E0000, 0x110000, 0x110000, 0x4B0000, 0x2A0000, 0x110000, 0x560000, 0x000000),
//                f(0x000000, 0x0F0000, 0x4C0000, 0x280000, 0x280000, 0x6B0000, 0x210000, 0x280000, 0x580000, 0x000000),
//                f(0x000000, 0x000000, 0x720000, 0x460000, 0x460000, 0x690000, 0x110000, 0x460000, 0x530000, 0x000000),
//                f(0x000000, 0x000000, 0x940000, 0x6D0000, 0x6D0000, 0x600000, 0x000000, 0x6D0000, 0x470000, 0x000000),
//                f(0x000000, 0x000000, 0x8A0000, 0x9B0000, 0x9B0000, 0x500000, 0x000000, 0x9B0000, 0x330000, 0x000000),
//                f(0x000000, 0x000000, 0x7A0000, 0xBC0000, 0xBC0000, 0x380000, 0x000000, 0xBC0000, 0x170000, 0x000000),
//                f(0x000000, 0x000000, 0x610000, 0xAB0000, 0xAB0000, 0x180000, 0x000000, 0xAB0000, 0x0D0000, 0x000000),
//                f(0x000000, 0x000000, 0x410000, 0x920000, 0x920000, 0x0F0000, 0x000000, 0x920000, 0x380000, 0x000000),
//                f(0x120000, 0x000000, 0x190000, 0x6E0000, 0x6E0000, 0x3C0000, 0x000000, 0x6E0000, 0x670000, 0x000000),
//                f(0x3E0000, 0x000000, 0x000000, 0x420000, 0x420000, 0x680000, 0x000000, 0x420000, 0x930000, 0x000000),
//                f(0x6A0000, 0x000000, 0x000000, 0x160000, 0x160000, 0x940000, 0x000000, 0x160000, 0xBF0000, 0x000000),
//                f(0x950000, 0x000000, 0x000000, 0x000000, 0x000000, 0xC00000, 0x000000, 0x160000, 0xEA0000, 0x000000),
//                f(0xC10000, 0x000000, 0x000000, 0x000000, 0x000000, 0xEC0000, 0x000000, 0x420000, 0xE80000, 0x000000),
//                f(0xED0000, 0x000000, 0x000000, 0x000000, 0x000000, 0xE60000, 0x000000, 0x6E0000, 0xBC0000, 0x000000),
//                f(0xD90000, 0x000000, 0x000000, 0x000000, 0x000000, 0xB00000, 0x000000, 0x920000, 0x880000, 0x000000),
//                f(0x9F0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x7B0000, 0x000000, 0xAB0000, 0x560000, 0x000000),
//                f(0x6D0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x4C0000, 0x000000, 0xBC0000, 0x2B0000, 0x000000),
//                f(0x430000, 0x000000, 0x000000, 0x000000, 0x000000, 0x250000, 0x000000, 0x9B0000, 0x080000, 0x000000),
//                f(0x200000, 0x000000, 0x000000, 0x000000, 0x000000, 0x060000, 0x000000, 0x6D0000, 0x130000, 0x000000),
//                f(0x050000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x460000, 0x270000, 0x000000),
//                f(0x0F0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x280000, 0x340000, 0x000000),
//                f(0x1B0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x110000, 0x390000, 0x000000),
//                f(0x200000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x010000, 0x360000, 0x000000),
//                f(0x1D0000, 0x000000, 0x000000, 0x070000, 0x070000, 0x000000, 0x000000, 0x000000, 0x2B0000, 0x000000),
//                f(0x120000, 0x000000, 0x000000, 0x070000, 0x070000, 0x000000, 0x000000, 0x000000, 0x120000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000)
//            )
//            )
        }

        "can render high density" {
            val effect = Effect(
                endFrame = 29,
                type = GlitterEffect.id,
                args = mapOf(
                    arg("density", .9f),
                    arg("lifetimeMs", 200)
                ),
                easing = Easing(LinearEasing.id, 0f, 100f),
                fill = Fill(
                    SingleColorFill.id,
                    BlendMode.NORMAL,
                    mapOf(
                        arg("color", "#ff0000")
                    )
                )
            )

            val renderedStagePropData = RenderUtils.render(effect, effect.endFrame + 1, 10)

// TODO
//            assertThat(
//                renderedStagePropData, hasRenderedFrames(
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x040000, 0x120000, 0x000000, 0x070000, 0x070000, 0x070000, 0x160000, 0x070000, 0x0B0000, 0x160000),
//                f(0x0F0000, 0x1D0000, 0x080000, 0x070000, 0x070000, 0x160000, 0x240000, 0x070000, 0x1E0000, 0x240000),
//                f(0x220000, 0x200000, 0x170000, 0x010000, 0x010000, 0x2D0000, 0x2B0000, 0x010000, 0x380000, 0x2B0000),
//                f(0x3D0000, 0x1B0000, 0x2E0000, 0x110000, 0x110000, 0x4B0000, 0x2A0000, 0x110000, 0x560000, 0x2A0000),
//                f(0x5F0000, 0x0F0000, 0x4C0000, 0x280000, 0x280000, 0x6B0000, 0x210000, 0x280000, 0x580000, 0x210000),
//                f(0x7F0000, 0x050000, 0x720000, 0x460000, 0x460000, 0x690000, 0x110000, 0x460000, 0x530000, 0x110000),
//                f(0x7A0000, 0x200000, 0x940000, 0x6D0000, 0x6D0000, 0x600000, 0x060000, 0x6D0000, 0x470000, 0x060000),
//                f(0x6D0000, 0x430000, 0x8A0000, 0x9B0000, 0x9B0000, 0x500000, 0x250000, 0x9B0000, 0x330000, 0x250000),
//                f(0x590000, 0x6D0000, 0x7A0000, 0xBC0000, 0xBC0000, 0x380000, 0x4C0000, 0xBC0000, 0x170000, 0x4C0000),
//                f(0x3D0000, 0x9F0000, 0x610000, 0xAB0000, 0xAB0000, 0x180000, 0x7B0000, 0xAB0000, 0x0D0000, 0x7B0000),
//                f(0x190000, 0xD90000, 0x410000, 0x920000, 0x920000, 0x0F0000, 0xB00000, 0x920000, 0x380000, 0xB00000),
//                f(0x120000, 0xED0000, 0x190000, 0x6E0000, 0x6E0000, 0x3C0000, 0xE60000, 0x6E0000, 0x670000, 0xE60000),
//                f(0x3E0000, 0xC10000, 0x130000, 0x420000, 0x420000, 0x680000, 0xEC0000, 0x420000, 0x930000, 0xEC0000),
//                f(0x6A0000, 0x950000, 0x3F0000, 0x160000, 0x160000, 0x940000, 0xC00000, 0x160000, 0xBF0000, 0xC00000),
//                f(0x950000, 0x6A0000, 0x6B0000, 0x160000, 0x000000, 0xC00000, 0x940000, 0x160000, 0xEA0000, 0x940000),
//                f(0xC10000, 0x3E0000, 0x970000, 0x420000, 0x000000, 0xEC0000, 0x680000, 0x420000, 0xE80000, 0x680000),
//                f(0xED0000, 0x120000, 0xC30000, 0x6E0000, 0x000000, 0xE60000, 0x3C0000, 0x6E0000, 0xBC0000, 0x3C0000),
//                f(0xD90000, 0x000000, 0xE30000, 0x920000, 0x000000, 0xB00000, 0x0F0000, 0x920000, 0x880000, 0x0F0000),
//                f(0x9F0000, 0x000000, 0xC40000, 0xAB0000, 0x000000, 0x7B0000, 0x180000, 0xAB0000, 0x560000, 0x180000),
//                f(0x6D0000, 0x000000, 0x8E0000, 0xBC0000, 0x000000, 0x4C0000, 0x380000, 0xBC0000, 0x2B0000, 0x380000),
//                f(0x430000, 0x000000, 0x600000, 0x9B0000, 0x000000, 0x250000, 0x500000, 0x9B0000, 0x080000, 0x500000),
//                f(0x200000, 0x000000, 0x390000, 0x6D0000, 0x000000, 0x060000, 0x600000, 0x6D0000, 0x130000, 0x600000),
//                f(0x050000, 0x000000, 0x1B0000, 0x460000, 0x000000, 0x110000, 0x690000, 0x460000, 0x270000, 0x690000),
//                f(0x0F0000, 0x000000, 0x030000, 0x280000, 0x000000, 0x210000, 0x6B0000, 0x280000, 0x340000, 0x6B0000),
//                f(0x1B0000, 0x000000, 0x0D0000, 0x110000, 0x000000, 0x2A0000, 0x4B0000, 0x110000, 0x390000, 0x4B0000),
//                f(0x200000, 0x000000, 0x150000, 0x010000, 0x000000, 0x2B0000, 0x2D0000, 0x010000, 0x360000, 0x2D0000),
//                f(0x1D0000, 0x000000, 0x150000, 0x070000, 0x070000, 0x240000, 0x160000, 0x000000, 0x2B0000, 0x160000),
//                f(0x120000, 0x000000, 0x0F0000, 0x070000, 0x070000, 0x160000, 0x070000, 0x000000, 0x120000, 0x070000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000)
//            )
//            )
        }

        "can render still frame" {
            val effect = Effect(
                endFrame = 29,
                type = GlitterEffect.id,
                args = mapOf(
                    arg("density", .5f),
                    arg("lifetimeMs", 200)
                ),
                easing = Easing(LinearEasing.id, 50f, 50f),
                fill = Fill(
                    SingleColorFill.id,
                    BlendMode.NORMAL,
                    mapOf(
                        arg("color", "#ff0000")
                    )
                )
            )

            val renderedStagePropData = RenderUtils.render(effect, effect.endFrame + 1, 10)
// TODO
//            assertThat(
//                renderedStagePropData, hasRenderedFrames(
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000),
//                f(0x800000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAA0000, 0x000000, 0x000000, 0xD50000, 0x000000)
//            )
//            )
        }
    }
}
