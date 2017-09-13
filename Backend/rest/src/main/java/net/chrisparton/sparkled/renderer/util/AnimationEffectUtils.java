package net.chrisparton.sparkled.renderer.util;

import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.entity.AnimationEffectParam;
import net.chrisparton.sparkled.entity.AnimationEffectTypeParamCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AnimationEffectUtils {

    private AnimationEffectUtils() {
    }

    public static String getEffectParamValue(AnimationEffect effect, AnimationEffectTypeParamCode effectTypeParamCode) {
        String value = getEffectParam(effect, effectTypeParamCode).getValue();
        return StringUtils.trimToEmpty(value);
    }

    public static List<String> getEffectParamMultiValue(AnimationEffect effect, AnimationEffectTypeParamCode effectTypeParamCode) {
        return getEffectParam(effect, effectTypeParamCode).getMultiValue();
    }

    private static AnimationEffectParam getEffectParam(AnimationEffect effect, AnimationEffectTypeParamCode effectTypeParamCode) {
        return effect.getParams()
                .stream()
                .filter(p -> effectTypeParamCode == p.getParamCode())
                .findFirst()
                .orElse(new AnimationEffectParam());
    }
}
