package io.sparkled.model.animation.fill.reference;

import io.sparkled.model.animation.fill.FillType;
import io.sparkled.model.animation.param.Param;
import io.sparkled.model.animation.param.ParamType;
import io.sparkled.model.animation.fill.FillTypeCode;
import io.sparkled.model.animation.param.ParamName;

import java.util.Arrays;
import java.util.List;

public class FillTypes {
    private static final List<FillType> TYPES = Arrays.asList(
            fill(FillTypeCode.RAINBOW, "Rainbow",
                    param(ParamName.CYCLE_COUNT, ParamType.DECIMAL).setValue(1),
                    param(ParamName.CYCLES_PER_SECOND, ParamType.DECIMAL).setValue(1)
            ),
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