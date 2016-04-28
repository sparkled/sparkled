package net.chrisparton.sparkled.entity;

import java.util.ArrayList;
import java.util.List;

public class AnimationEffectChannel {

    private List<AnimationEffect> effects = new ArrayList<>();
    private String name;
    private int startLed = 0;
    private int endLed = 0;
    private int displayOrder;

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    public AnimationEffectChannel() {
    }

    public AnimationEffectChannel(String name, int displayOrder, int startLed, int endLed) {
        this.name = name;
        this.displayOrder = displayOrder;
        this.startLed = startLed;
        this.endLed = endLed;
    }

    public List<AnimationEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<AnimationEffect> effects) {
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getStartLed() {
        return startLed;
    }

    public void setStartLed(int startLed) {
        this.startLed = startLed;
    }

    public int getEndLed() {
        return endLed;
    }

    public void setEndLed(int endLed) {
        this.endLed = endLed;
    }
}
