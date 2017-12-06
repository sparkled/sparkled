package net.chrisparton.sparkled.model.animation.fill.reference;

import net.chrisparton.sparkled.model.animation.fill.FillType;
import net.chrisparton.sparkled.model.animation.fill.FillTypeCode;
import net.chrisparton.sparkled.model.animation.param.Param;
import net.chrisparton.sparkled.model.animation.param.ParamName;
import net.chrisparton.sparkled.model.animation.param.ParamType;

import java.util.Arrays;
import java.util.List;

public class FillTypes {
    private static final List<FillType> TYPES = Arrays.asList(
            fill(FillTypeCode.RAINBOW, "Rainbow"),
            fill(FillTypeCode.SOLID, "Solid",
                    param(ParamName.COLOR, ParamType.COLOR)
            )
    );

    public static List<FillType> get() {
        return TYPES;
    }

    private static FillType fill(FillTypeCode fillType, String name, Param... params) {
        return new FillType().setCode(fillType).setName(name).setParams(Arrays.asList(params));
    }

    private static Param param(ParamName paramName, ParamType type) {
        return new Param().setName(paramName.getName()).setType(type);
    }
}