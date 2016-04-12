package net.chrisparton.xmas.entity;

import java.util.Map;

public class AnimationEffect {

    private AnimationEffectType effectType;
    private Map<AnimationEffectTypeParam, String> params;
    private int startFrame;
    private int endFrame;

    public AnimationEffect() {
    }

    public AnimationEffectType getEffectType() {
        return effectType;
    }

    public void setEffectType(AnimationEffectType effectType) {
        this.effectType = effectType;
    }

    public Map<AnimationEffectTypeParam, String> getParams() {
        return params;
    }

    public void setParams(Map<AnimationEffectTypeParam, String> params) {
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
