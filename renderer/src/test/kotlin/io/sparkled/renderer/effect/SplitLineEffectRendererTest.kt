package io.sparkled.renderer.effect;

import io.sparkled.model.animation.easing.Easing;
import io.sparkled.model.animation.easing.EasingTypeCode;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectTypeCode;
import io.sparkled.model.animation.fill.Fill;
import io.sparkled.model.animation.fill.FillTypeCode;
import io.sparkled.model.animation.param.Param;
import io.sparkled.model.animation.param.ParamName;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.util.RenderUtils;
import io.sparkled.util.matchers.SparkledMatchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class SplitLineEffectRendererTest {

    @Test
    public void can_render_5_led_line_on_10_led_channel() {
        Effect effect = new Effect()
                .setType(EffectTypeCode.SPLIT_LINE)
                .setParams(
                        new Param().setName(ParamName.LENGTH).setValue(5)
                )
                .setEasing(
                        new Easing()
                                .setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        new Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(
                                        new Param().setName(ParamName.COLOR).setValue("#ffffff")
                                )
                );

        RenderedStagePropData renderedStagePropData = RenderUtils.render(effect, 50, 10);

        final int[] c = new int[]{0x000000, 0xFFFFFF};
        assertThat(renderedStagePropData, SparkledMatchers.hasLeds(new int[][]{
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]}
        }));
    }

    @Test
    public void can_render_5_led_line_on_11_led_channel() {
        Effect effect = new Effect()
                .setType(EffectTypeCode.SPLIT_LINE)
                .setParams(
                        new Param().setName(ParamName.LENGTH).setValue(5)
                )
                .setEasing(
                        new Easing().setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        new Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(
                                        new Param().setName(ParamName.COLOR).setValue("#ffffff")
                                )
                );

        RenderedStagePropData renderedStagePropData = RenderUtils.render(effect, 50, 11);

        final int[] c = new int[]{0x000000, 0xFFFFFF};
        assertThat(renderedStagePropData, SparkledMatchers.hasLeds(new int[][]{
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]}
        }));
    }
}
