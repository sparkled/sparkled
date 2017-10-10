package net.chrisparton.sparkled.model.animation;

public class AnimationEasingType {

    private String code;
    private String name;
    private AnimationEasing easing;

    AnimationEasingType(String code, String name, AnimationEasing easing) {
        this.code = code;
        this.name = name;
        this.easing = easing;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public AnimationEasing getEasing() {
        return easing;
    }
}