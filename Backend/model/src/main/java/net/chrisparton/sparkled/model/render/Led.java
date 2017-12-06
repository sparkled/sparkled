package net.chrisparton.sparkled.model.render;

import java.awt.*;
import java.util.Objects;

public class Led {

    private static final int R = 0;
    private static final int G = 1;
    private static final int B = 2;

    private final byte[] ledData;
    private final int ledNumber;
    private final int index;

    public Led(byte[] ledData, int ledNumber) {
        this.ledData = ledData;
        this.ledNumber = ledNumber;
        this.index = ledNumber * 3;
    }

    public void addColor(Color color) {
        addRgb(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void addRgb(int r, int g, int b) {
        setR(Math.min(getR() + r, 255));
        setG(Math.min(getG() + g, 255));
        setB(Math.min(getB() + b, 255));
    }

    public int getLedNumber() {
        return ledNumber;
    }

    public int getR() {
        return ledData[index + R] & 0xFF;
    }

    private void setR(int r) {
        ledData[index + R] = (byte) r;
    }

    public int getG() {
        return ledData[index + G] & 0xFF;
    }

    private void setG(int g) {
        ledData[index + G] = (byte) g;
    }

    public int getB() {
        return ledData[index + B] & 0xFF;
    }

    private void setB(int b) {
        ledData[index + B] = (byte) b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Led)) {
            return false;
        }

        Led led = (Led) o;
        return getR() == led.getR() && getG() == led.getG() && getB() == led.getB();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getR(), getG(), getB());
    }

    @Override
    public String toString() {
        return String.format("#%02X%02X%02X", getR(), getG(), getB());
    }
}
