package net.chrisparton.sparkled.renderer.easing.function;

import net.chrisparton.sparkled.model.animation.easing.Easing;
import net.chrisparton.sparkled.model.animation.param.ParamName;
import net.chrisparton.sparkled.renderer.easing.EasingFunction;
import net.chrisparton.sparkled.renderer.util.ParamUtils;

public class ConstantEasing implements EasingFunction {
    @Override
    public float getProgress(Easing easing, float currentFrame, float durationFrames) {
        return ParamUtils.getDecimalValue(easing, ParamName.PERCENT) / 100f;
    }
}
