package net.chrisparton.xmas.renderer.data;

public class Led {

    private short r = 0;
    private short g = 0;
    private short b = 0;

    public Led() {
    }

    public Led(int r, int g, int b) {
        this.r = (short) r;
        this.g = (short) g;
        this.b = (short) b;
    }

    public Led(Led led) {
        this.r = led.r;
        this.g = led.g;
        this.b = led.b;
    }

    public short getR() {
        return r;
    }

    public void setR(short r) {
        this.r = r;
    }

    public short getG() {
        return g;
    }

    public void setG(short g) {
        this.g = g;
    }

    public short getB() {
        return b;
    }

    public void setB(short b) {
        this.b = b;
    }
}
