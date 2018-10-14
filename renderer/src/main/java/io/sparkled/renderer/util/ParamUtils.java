package io.sparkled.renderer.util;

import io.sparkled.model.animation.param.HasParams;
import io.sparkled.model.animation.param.Param;
import io.sparkled.model.animation.param.ParamName;

import java.awt.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ParamUtils {

    private ParamUtils() {
    }

    public static int getIntegerValue(HasParams parent, ParamName paramName) {
        Param param = getParam(parent, paramName);
        List<String> value = param.getValue();
        return Integer.parseInt(value.get(0));
    }

    public static float getDecimalValue(HasParams parent, ParamName paramName) {
        Param param = getParam(parent, paramName);
        List<String> value = param.getValue();
        return Float.parseFloat(value.get(0));
    }

    public static Color getColorValue(HasParams parent, ParamName paramName) {
        return getColorsValue(parent, paramName).get(0);
    }

    private static List<Color> getColorsValue(HasParams parent, ParamName paramName) {
        Param param = getParam(parent, paramName);
        List<String> values = param.getValue();

        return values.stream()
                .map(ParamUtils::convertColor)
                .collect(toList());
    }

    private static Param getParam(HasParams parent, ParamName paramName) {
        return parent.getParams()
                .stream()
                .filter(p -> paramName == p.getName())
                .findFirst()
                .orElse(new Param());
    }

    private static Color convertColor(String hexColor) {
        return Color.decode(hexColor.toLowerCase());
    }
}
