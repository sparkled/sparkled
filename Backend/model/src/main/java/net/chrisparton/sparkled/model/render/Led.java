package net.chrisparton.sparkled.model.render;

import java.awt.*;
import java.util.Objects;

public class Led {

    private static final int R = 0;
    private static final int G = 1;
    private static final int B = 2;

    private byte[] ledData;
    private int ledIndex;

    public Led(byte[] ledData, int ledIndex) {
        this.ledData = ledData;
        this.ledIndex = ledIndex * 3;
    }

    public void addRgb(Color color) {
        addRgb(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void addRgb(int r, int g, int b) {
        setR(Math.min(getR() + r, 255));
        setG(Math.min(getG() + g, 255));
        setB(Math.min(getB() + b, 255));
    }

    public int getR() {
        return ledData[ledIndex + R] & 0xFF;
    }

    private void setR(int r) {
        ledData[ledIndex + R] = (byte) r;
    }

    public int getG() {
        return ledData[ledIndex + G] & 0xFF;
    }

    private void setG(int g) {
        ledData[ledIndex + G] = (byte) g;
    }

    public int getB() {
        return ledData[ledIndex + B] & 0xFF;
    }

    private void setB(int b) {
        ledData[ledIndex + B] = (byte) b;
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
