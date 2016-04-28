package net.chrisparton.sparkled.entity;

public enum AnimationEffectTypeParamCode {

    COLOUR(new AnimationEffectTypeParam("COLOUR", "Colour")),
    MULTI_COLOUR(new AnimationEffectTypeParam("COLOURS", "Colours"));

    private AnimationEffectTypeParam effectTypeParam;

    AnimationEffectTypeParamCode(AnimationEffectTypeParam effectTypeParam) {
        this.effectTypeParam = effectTypeParam;
    }

    public AnimationEffectTypeParam getEffectTypeParam() {
        return effectTypeParam;
    }
}
