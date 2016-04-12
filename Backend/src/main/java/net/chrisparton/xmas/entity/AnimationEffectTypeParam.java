package net.chrisparton.xmas.entity;

public enum AnimationEffectTypeParam {
    COLOUR("Colour"),
    MULTI_COLOUR("Colours");

    private String name;

    AnimationEffectTypeParam(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
