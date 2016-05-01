package net.chrisparton.sparkled.renderer.effect;

public class LineLeftEffectRenderer extends AbstractLineEffectRenderer {

    @Override
    protected double calculateProgressPercentage(double progress) {
        return 100 - 100 * progress;
    }
}
