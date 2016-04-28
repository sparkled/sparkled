package net.chrisparton.sparkled.entity;

import java.util.ArrayList;
import java.util.List;

public class AnimationEffect {

    private AnimationEffectTypeCode effectType;
    private List<AnimationEffectParam> params = new ArrayList<>();
    private int startFrame;
    private int endFrame;

    public AnimationEffect() {
    }

    public AnimationEffectTypeCode getEffectType() {
        return effectType;
    }

    public void setEffectType(AnimationEffectTypeCode effectType) {
        this.effectType = effectType;
    }

    public List<AnimationEffectParam> getParams() {
        return params;
    }

    public void setParams(List<AnimationEffectParam> params) {
        this.params = params;
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
}
