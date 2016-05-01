package net.chrisparton.sparkled.converter;

import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.entity.AnimationEffectTypeParamCode;
import net.chrisparton.sparkled.renderer.util.AnimationEffectUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColoursParamConverter extends EffectTypeParamConverter<List<Color>> {

    @Override
    public List<Color> convert(AnimationEffect effect) {
        List<Color> colours = new ArrayList<>();

        String value = AnimationEffectUtils.getEffectParamValue(effect, AnimationEffectTypeParamCode.MULTI_COLOUR);
        if (value.isEmpty()) {
            value = AnimationEffectUtils.getEffectParamValue(effect, AnimationEffectTypeParamCode.COLOUR);
        }

        String[] hexColours = value.split(";");
        for (String hexColour : hexColours) {
            Color colour = convertColour(hexColour);
            colours.add(colour);
        }

        return colours;
    }

    private Color convertColour(String hexColour) {
        return hexColour.isEmpty() ? Color.BLACK : Color.decode('#' + hexColour);
    }
}
