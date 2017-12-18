package io.sparkled.renderer.effect;

import io.sparkled.model.animation.easing.Easing;
import io.sparkled.model.animation.easing.EasingTypeCode;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectTypeCode;
import io.sparkled.model.animation.fill.Fill;
import io.sparkled.model.animation.fill.FillTypeCode;
import io.sparkled.model.animation.param.Param;
import io.sparkled.model.animation.param.ParamName;
import io.sparkled.model.render.RenderedChannel;
import io.sparkled.util.RenderUtils;
import io.sparkled.util.matchers.SparkledMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static io.sparkled.util.matchers.SparkledMatchers.hasLeds;
import static org.hamcrest.MatcherAssert.assertThat;

public class LineEffectRendererTest {

    @Test
    public void can_render_1_led_line_on_10_led_channel() {
        Effect effect = new Effect()
                .setType(EffectTypeCode.LINE)
                .setParams(
                        new Param().setName(ParamName.LENGTH.getName()).setValue(1)
                )
                .setEasing(
                        new Easing()
                                .setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        new Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(
                                        new Param().setName(ParamName.COLOR.getName()).setValue("#ffffff")
                                )
                );

        RenderedChannel renderedChannel = RenderUtils.render(effect, 12, 10);

        final int[] c = new int[]{0x000000, 0xFFFFFF};
        MatcherAssert.assertThat(renderedChannel, SparkledMatchers.hasLeds(new int[][]{
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[0]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]}
        }));
    }

    @Test
    public void can_render_20_led_line_on_10_led_channel() {
        Effect effect = new Effect()
                .setType(EffectTypeCode.LINE)
                .setParams(
                        new Param().setName(ParamName.LENGTH.getName()).setValue(20)
                )
                .setEasing(
                        new Easing()
                                .setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        new Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(
                                        new Param().setName(ParamName.COLOR.getName()).setValue("#ffffff")
                                )
                );

        RenderedChannel renderedChannel = RenderUtils.render(effect, 20, 10);

        final int[] c = new int[]{0x000000, 0xFFFFFF};
        MatcherAssert.assertThat(renderedChannel, SparkledMatchers.hasLeds(new int[][]{
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]}
        }));
    }
}
