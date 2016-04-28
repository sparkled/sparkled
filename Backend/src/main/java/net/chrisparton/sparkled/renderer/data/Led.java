package net.chrisparton.sparkled.renderer.data;

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

    public void addRgb(int r, int g, int b) {
        this.r = (short) Math.min(this.r + r, 255);
        this.g = (short) Math.min(this.g + g, 255);
        this.b = (short) Math.min(this.b + b, 255);
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
