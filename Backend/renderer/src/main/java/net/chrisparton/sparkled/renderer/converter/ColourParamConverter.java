package net.chrisparton.sparkled.renderer.converter;

import net.chrisparton.sparkled.model.animation.AnimationEffect;

import java.awt.*;
import java.util.List;

public class ColourParamConverter extends EffectTypeParamConverter<Color> {

    @Override
    public Color convert(AnimationEffect effect) {
        List<Color> colours = new ColoursParamConverter().convert(effect);
        return colours.isEmpty() ? null : colours.get(0);
    }
}
