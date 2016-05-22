package net.chrisparton.sparkled.converter;

import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.entity.AnimationEffectTypeParamCode;
import net.chrisparton.sparkled.renderer.util.AnimationEffectUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColoursParamConverter extends EffectTypeParamConverter<List<Color>> {

    @Override
    public List<Color> convert(AnimationEffect effect) {
        List<String> values = AnimationEffectUtils.getEffectParamMultiValue(effect, AnimationEffectTypeParamCode.MULTI_COLOUR);

        if (values.isEmpty()) {
            String value = AnimationEffectUtils.getEffectParamValue(effect, AnimationEffectTypeParamCode.COLOUR);
            values = Collections.singletonList(value);
        }

        return values.stream()
                .map(this::convertColour)
                .collect(Collectors.toList());
    }

    private Color convertColour(String hexColour) {
        return hexColour.isEmpty() ? Color.BLACK : Color.decode(hexColour);
    }
}
