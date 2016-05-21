package net.chrisparton.sparkled.entity;

import java.util.ArrayList;
import java.util.List;

public class AnimationEffect {

    private AnimationEffectTypeCode effectType;
    private AnimationEasingTypeCode easingType;
    private int startFrame;
    private int endFrame;
    private int repetitions = 1;
    private boolean reverse = false;
    private List<AnimationEffectParam> params = new ArrayList<>();

    public AnimationEffect() {
    }

    public AnimationEffectTypeCode getEffectType() {
        return effectType;
    }

    public void setEffectType(AnimationEffectTypeCode effectType) {
        this.effectType = effectType;
    }

    public AnimationEasingTypeCode getEasingType() {
        return easingType;
    }

    public void setEasingType(AnimationEasingTypeCode easingType) {
        this.easingType = easingType;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public List<AnimationEffectParam> getParams() {
        return params;
    }

    public void setParams(List<AnimationEffectParam> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "AnimationEffect{" +
                "effectType=" + effectType +
                ", easingType=" + easingType +
                ", startFrame=" + startFrame +
                ", endFrame=" + endFrame +
                ", repetitions=" + repetitions +
                ", reverse=" + reverse +
                ", params=" + params +
                '}';
    }
}
