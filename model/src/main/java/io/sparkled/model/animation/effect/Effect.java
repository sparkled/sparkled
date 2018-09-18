package io.sparkled.model.animation.effect;

import io.sparkled.model.animation.easing.Easing;
import io.sparkled.model.animation.fill.Fill;
import io.sparkled.model.animation.param.HasParams;
import io.sparkled.model.animation.param.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Effect implements HasParams {

    private EffectTypeCode type;
    private List<Param> params = new ArrayList<>();
    private Easing easing;
    private Fill fill = new Fill();
    private int startFrame;
    private int endFrame;
    private int repetitions = 1;
    private boolean reverse = false;

    public EffectTypeCode getType() {
        return type;
    }

    public Effect setType(EffectTypeCode type) {
        this.type = type;
        return this;
    }

    @Override
    public List<Param> getParams() {
        return params;
    }

    public Effect setParams(List<Param> params) {
        this.params = params;
        return this;
    }

    public Effect setParams(Param... params) {
        return setParams(Arrays.asList(params));
    }

    public Easing getEasing() {
        return easing;
    }

    public Effect setEasing(Easing easing) {
        this.easing = easing;
        return this;
    }

    public Fill getFill() {
        return fill;
    }

    public Effect setFill(Fill fill) {
        this.fill = fill;
        return this;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public Effect setStartFrame(int startFrame) {
        this.startFrame = startFrame;
        return this;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public Effect setEndFrame(int endFrame) {
        this.endFrame = endFrame;
        return this;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public Effect setRepetitions(int repetitions) {
        this.repetitions = repetitions;
        return this;
    }

    public boolean isReverse() {
        return reverse;
    }

    public Effect setReverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Effect)) return false;
        Effect effect = (Effect) o;
        return startFrame == effect.startFrame &&
                endFrame == effect.endFrame &&
                repetitions == effect.repetitions &&
                reverse == effect.reverse &&
                type == effect.type &&
                Objects.equals(params, effect.params) &&
                easing == effect.easing &&
                Objects.equals(fill, effect.fill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, params, easing, fill, startFrame, endFrame, repetitions, reverse);
    }

    @Override
    public String toString() {
        return "Effect{" +
                "type=" + type +
                ", params=" + params +
                ", easing=" + easing +
                ", fill=" + fill +
                ", startFrame=" + startFrame +
                ", endFrame=" + endFrame +
                ", repetitions=" + repetitions +
                ", reverse=" + reverse +
                '}';
    }
}
