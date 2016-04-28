package net.chrisparton.sparkled.converter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColoursParamConverter extends EffectTypeParamConverter<List<Color>> {

    @Override
    public List<Color> convert(String value) {
        List<Color> colours = new ArrayList<>();

        String[] hexColours = value.split(";");
        for (String hexColour : hexColours) {
            Color colour = convertColour(hexColour);
            colours.add(colour);
        }

        return colours;
    }

    private Color convertColour(String hexColour) {
        return Color.decode('#' + hexColour);
    }
}
