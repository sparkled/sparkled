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
import org.hamcrest.MatcherAssert;
import org.junit.Test;

public class FlashEffectRendererTest {

    @Test
    public void can_render() {
        Effect effect = new Effect()
                .setType(EffectTypeCode.FLASH)
                .setEasing(new Easing().setType(EasingTypeCode.LINEAR))
                .setFill(new Fill()
                        .setType(FillTypeCode.SOLID)
                        .setParams(
                                new Param().setName(ParamName.COLOR.getName()).setValue("#ffffff")
                        )
                );

        RenderedStagePropData renderedStagePropData = RenderUtils.render(effect, 11, 10);

        final int[] c = new int[]{0x000000, 0x333333, 0x666666, 0x999999, 0xCCCCCC, 0xFFFFFF};
        MatcherAssert.assertThat(renderedStagePropData, SparkledMatchers.hasLeds(new int[][]{
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
