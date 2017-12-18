package io.sparkled.model.animation.param;

public enum ParamName {
    COLOR("Color"),
    CYCLE_COUNT("Cycle Count"),
    CYCLES_PER_SECOND("Cycles Per Second"),
    LENGTH("Length"),
    PERCENT("Percent");

    private String name;

    ParamName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
