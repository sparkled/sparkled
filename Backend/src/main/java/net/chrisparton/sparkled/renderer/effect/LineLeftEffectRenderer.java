package net.chrisparton.sparkled.renderer.effect;

public class LineLeftEffectRenderer extends AbstractLineEffectRenderer {

    @Override
    protected int calculateProgressPercentage(int progress) {
        return 100 - progress;
    }
}
