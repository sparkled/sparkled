package net.chrisparton.sparkled.renderer.fill;

import net.chrisparton.sparkled.model.animation.fill.FillTypeCode;
import net.chrisparton.sparkled.renderer.fill.function.RainbowFill;
import net.chrisparton.sparkled.renderer.fill.function.SolidFill;

import java.util.HashMap;
import java.util.Map;

public class FillFunctions {

    private static Map<FillTypeCode, FillFunction> functions = new HashMap<>();

    static {
        functions.put(FillTypeCode.RAINBOW, new RainbowFill());
        functions.put(FillTypeCode.SOLID, new SolidFill());
    }

    public static FillFunction get(FillTypeCode fillType) {
        return functions.get(fillType);
    }
}