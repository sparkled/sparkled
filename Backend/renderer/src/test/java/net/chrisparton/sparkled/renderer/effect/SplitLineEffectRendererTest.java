package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.model.animation.easing.Easing;
import net.chrisparton.sparkled.model.animation.easing.EasingTypeCode;
import net.chrisparton.sparkled.model.animation.effect.Effect;
import net.chrisparton.sparkled.model.animation.effect.EffectTypeCode;
import net.chrisparton.sparkled.model.animation.fill.Fill;
import net.chrisparton.sparkled.model.animation.fill.FillTypeCode;
import net.chrisparton.sparkled.model.animation.param.Param;
import net.chrisparton.sparkled.model.animation.param.ParamName;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.util.RenderUtils;
import org.junit.Test;

import static net.chrisparton.sparkled.util.matchers.SparkledMatchers.hasLeds;
import static org.hamcrest.MatcherAssert.assertThat;

public class SplitLineEffectRendererTest {

    @Test
    public void can_render_5_led_line_on_10_led_channel() {
        Effect effect = new Effect()
                .setType(EffectTypeCode.SPLIT_LINE)
                .setParams(
                        new Param().setName(ParamName.LENGTH.getName()).setValue(5)
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

        RenderedChannel renderedChannel = RenderUtils.render(effect, 50, 10);

        final int[] c = new int[]{0x000000, 0xFFFFFF};
        assertThat(renderedChannel, hasLeds(new int[][]{
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
                        new Param().setName(ParamName.LENGTH.getName()).setValue(5)
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

        RenderedChannel renderedChannel = RenderUtils.render(effect, 50, 11);

        final int[] c = new int[]{0x000000, 0xFFFFFF};
        assertThat(renderedChannel, hasLeds(new int[][]{
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
