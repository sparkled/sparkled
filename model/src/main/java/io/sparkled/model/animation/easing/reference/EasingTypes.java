package io.sparkled.model.animation.easing.reference;

import io.sparkled.model.animation.easing.EasingType;
import io.sparkled.model.animation.easing.EasingTypeCode;
import io.sparkled.model.animation.param.Param;
import io.sparkled.model.animation.param.ParamName;
import io.sparkled.model.animation.param.ParamType;

import java.util.Arrays;
import java.util.List;

public class EasingTypes {
    private static final List<EasingType> TYPES = Arrays.asList(
            easingType(EasingTypeCode.CONSTANT, "Constant",
                    param(ParamName.PERCENT, ParamType.DECIMAL).setValue(50f)
            ),
            easingType(EasingTypeCode.LINEAR, "Linear")
    );

    public static List<EasingType> get() {
        return TYPES;
    }

    private static EasingType easingType(EasingTypeCode easingType, String name, Param... params) {
        return new EasingType().setCode(easingType).setName(name).setParams(Arrays.asList(params));
    }

    private static Param param(ParamName paramName, ParamType type) {
        return new Param().setName(paramName.getName()).setType(type);
    }
}