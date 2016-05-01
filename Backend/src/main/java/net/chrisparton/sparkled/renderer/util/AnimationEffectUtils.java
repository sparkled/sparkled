package net.chrisparton.sparkled.renderer.util;

import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.entity.AnimationEffectParam;
import net.chrisparton.sparkled.entity.AnimationEffectTypeParamCode;
import org.apache.commons.lang3.StringUtils;

public class AnimationEffectUtils {

    private AnimationEffectUtils() {
    }

    public static String getEffectParamValue(AnimationEffect effect, AnimationEffectTypeParamCode effectTypeParamCode) {
        String value = effect.getParams()
                .stream()
                .filter(p -> effectTypeParamCode == p.getParamCode())
                .findFirst()
                .orElse(new AnimationEffectParam())
                .getValue();

        return StringUtils.trimToEmpty(value);
    }
}
