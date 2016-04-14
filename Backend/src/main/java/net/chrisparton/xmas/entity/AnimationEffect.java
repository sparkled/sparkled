package net.chrisparton.xmas.entity;

import java.util.List;

public class AnimationEffect {

    private String effectType;
    private List<AnimationEffectParam> params;
    private int startFrame;
    private int endFrame;

    public AnimationEffect() {
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
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
