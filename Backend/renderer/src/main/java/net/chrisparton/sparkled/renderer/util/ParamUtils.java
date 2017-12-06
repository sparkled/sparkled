package net.chrisparton.sparkled.renderer.util;

import net.chrisparton.sparkled.model.animation.param.HasParams;
import net.chrisparton.sparkled.model.animation.param.Param;
import net.chrisparton.sparkled.model.animation.param.ParamName;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ParamUtils {

    private ParamUtils() {
    }

    public static int getIntegerValue(HasParams parent, ParamName paramName) {
        Param param = getParam(parent, paramName);
        List<String> value = param.getValue();
        return Integer.parseInt(value.get(0));
    }

    public static Color getColorValue(HasParams parent, ParamName paramName) {
        return getColorsValue(parent, paramName).get(0);
    }

    private static List<Color> getColorsValue(HasParams parent, ParamName paramName) {
        Param param = getParam(parent, paramName);
        List<String> values = param.getValue();

        return values.stream()
                .map(ParamUtils::convertColor)
                .collect(Collectors.toList());
    }

    private static Param getParam(HasParams parent, ParamName paramName) {
        return parent.getParams()
                .stream()
                .filter(p -> paramName.getName().equals(p.getName()))
                .findFirst()
                .orElse(new Param());
    }

    private static Color convertColor(String hexColor) {
        return Color.decode(hexColor);
    }
}
