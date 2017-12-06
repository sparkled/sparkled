package net.chrisparton.sparkled.renderer.effect;

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

public class FlashEffectRendererTest {

    @Test
    public void can_render() {
        Effect effect = new Effect()
                .setType(EffectTypeCode.FLASH)
                .setEasing(EasingTypeCode.LINEAR)
                .setFill(
                        new Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(
                                        new Param().setName(ParamName.COLOR.getName()).setValue("#ffffff")
                                )
                );

        RenderedChannel renderedChannel = RenderUtils.render(effect, 11, 10);

        final int[] c = new int[]{0x000000, 0x333333, 0x666666, 0x999999, 0xCCCCCC, 0xFFFFFF};
        assertThat(renderedChannel, hasLeds(new int[][]{
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2]},
                {c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3]},
                {c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4]},
                {c[5], c[5], c[5], c[5], c[5], c[5], c[5], c[5], c[5], c[5]},
                {c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4]},
                {c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3]},
                {c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2]},
                {c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]},
                {c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]}
        }));
    }
}
