package net.chrisparton.sparkled.converter;

import net.chrisparton.sparkled.model.animation.AnimationEffect;
import net.chrisparton.sparkled.model.animation.AnimationEffectTypeParamCode;
import net.chrisparton.sparkled.renderer.util.AnimationEffectUtils;

import java.util.logging.Logger;

public class LengthParamConverter extends EffectTypeParamConverter<Integer> {

    Logger logger = Logger.getLogger(LengthParamConverter.class.getName());

    @Override
    public Integer convert(AnimationEffect effect) {
        String value = AnimationEffectUtils.getEffectParamValue(effect, AnimationEffectTypeParamCode.LENGTH);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse '" + value + "' as an integer for effect " + effect);
            return 1;
        }
    }
}
