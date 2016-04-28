package net.chrisparton.sparkled.converter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColourParamConverter extends EffectTypeParamConverter<Color> {

    @Override
    public Color convert(String value) {
        List<Color> colours = new ColoursParamConverter().convert(value);

        return colours.isEmpty() ? null : colours.get(0);
    }
}
