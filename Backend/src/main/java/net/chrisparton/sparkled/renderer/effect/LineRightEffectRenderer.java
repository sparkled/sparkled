package net.chrisparton.sparkled.renderer.effect;

public class LineRightEffectRenderer extends AbstractLineEffectRenderer {

    @Override
    protected double calculateProgressPercentage(double progress) {
        return 100 * progress;
    }
}
