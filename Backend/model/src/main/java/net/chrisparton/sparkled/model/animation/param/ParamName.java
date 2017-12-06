package net.chrisparton.sparkled.model.animation.param;

public enum ParamName {
    LENGTH("Length"),
    COLOR("Color");

    private String name;

    ParamName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
