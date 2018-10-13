package io.sparkled.renderer.easing.function;

import io.sparkled.model.animation.easing.Easing;
import io.sparkled.model.animation.param.ParamName;
import io.sparkled.renderer.easing.EasingFunction;
import io.sparkled.renderer.util.ParamUtils;

public class ConstantEasing implements EasingFunction {
    @Override
    public float getProgress(Easing easing, float currentFrame, float frameCount) {
        return ParamUtils.getDecimalValue(easing, ParamName.PERCENT) / 100f;
    }
}
