package net.chrisparton.sparkled.entity;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum AnimationEasingTypeCode {

    LINEAR(new AnimationEasingType("LINEAR", "Linear", (t, b, c, d) -> {
        return c * t / d + b;
    })),

    CONSTANT_MIDPOINT(new AnimationEasingType("MIDPOINT", "Constant Midpoint (50%)", (t, b, c, d) -> .5)),

    EASE_IN_EXPO(new AnimationEasingType("EASE_IN_EXPO", "Ease In: Exponential", (t, b, c, d) -> {
        return (t==0) ? b : c * (float)Math.pow(2, 10 * (t/d - 1)) + b;
    })),

    EASE_OUT_EXPO(new AnimationEasingType("EASE_OUT_EXPO", "Ease Out: Exponential", (t, b, c, d) -> {
        return (t==d) ? b+c : c * (-(float)Math.pow(2, -10 * t/d) + 1) + b;
    })),

    EASE_IN_OUT_CIRC(new AnimationEasingType("EASE_IN_OUT_CIRC", "Circ", (t, b, c, d) -> {
        if ((t /= d / 2) < 1) return -c / 2 * (Math.sqrt(1 - t * t) - 1) + b;
        return c / 2 * (Math.sqrt(1 - (t -= 2) * t) + 1) + b;
    })),

    EASE_IN_OUT_CUBIC(new AnimationEasingType("EASE_IN_OUT_CUBIC", "Cubic", (t, b, c, d) -> {
        if ((t /= d / 2) < 1) return c / 2 * t * t * t + b;
        return c / 2 * ((t -= 2) * t * t + 2) + b;
    })),

    EASE_IN_OUT_EXPO(new AnimationEasingType("EASE_IN_OUT_EXPO", "Expo", (t, b, c, d) -> {
        if (t == 0) return b;
        if (t == d) return b + c;
        if ((t /= d / 2) < 1) return c / 2 * Math.pow(2, 10 * (t - 1)) + b;
        return c / 2 * (-Math.pow(2, -10 * --t) + 2) + b;
    })),

    EASE_IN_OUT_QUAD(new AnimationEasingType("EASE_IN_OUT_QUAD", "Quad", (t, b, c, d) -> {
        if ((t /= d / 2) < 1) return c / 2 * t * t + b;
        return -c / 2 * ((--t) * (t - 2) - 1) + b;
    })),

    EASE_IN_OUT_QUART(new AnimationEasingType("EASE_IN_OUT_QUART", "Quart", (t, b, c, d) -> {
        if ((t /= d / 2) < 1) return c / 2 * t * t * t * t + b;
        return -c / 2 * ((t -= 2) * t * t * t - 2) + b;
    })),

    EASE_IN_OUT_QUINT(new AnimationEasingType("EASE_IN_OUT_QUINT", "Quint", (t, b, c, d) -> {
        if ((t /= d / 2) < 1) return c / 2 * t * t * t * t * t + b;
        return c / 2 * ((t -= 2) * t * t * t * t + 2) + b;
    })),

    EASE_IN_OUT_SINE(new AnimationEasingType("EASE_IN_OUT_SINE", "Sine", (t, b, c, d) -> {
        return -c / 2 * (Math.cos(Math.PI * t / d) - 1) + b;
    }));

    public static final List<AnimationEasingType> EASING_TYPES = Arrays.asList(AnimationEasingTypeCode.values())
            .stream()
            .map(AnimationEasingTypeCode::getEasingType)
            .collect(toList());

    private AnimationEasingType easingType;

    AnimationEasingTypeCode(AnimationEasingType easingType) {
        this.easingType = easingType;
    }

    public AnimationEasingType getEasingType() {
        return easingType;
    }
}