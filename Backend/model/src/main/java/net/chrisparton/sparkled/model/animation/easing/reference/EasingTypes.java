package net.chrisparton.sparkled.model.animation.easing.reference;

import net.chrisparton.sparkled.model.animation.easing.EasingType;
import net.chrisparton.sparkled.model.animation.easing.EasingTypeCode;

import java.util.Arrays;
import java.util.List;

public class EasingTypes {
    private static final List<EasingType> TYPES = Arrays.asList(
            easingType(EasingTypeCode.CONSTANT_MIDPOINT, "Constant Midpoint"),
            easingType(EasingTypeCode.LINEAR, "Linear")
    );

    public static List<EasingType> get() {
        return TYPES;
    }

    private static EasingType easingType(EasingTypeCode easingType, String name) {
        return new EasingType().setCode(easingType).setName(name);
    }
}