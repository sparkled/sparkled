package net.chrisparton.sparkled.renderer.easing.function;

import org.junit.Test;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class LinearEasingTest {

    @Test
    public void can_render() {
        LinearEasing linearEasing = new LinearEasing();
        float[] expectedResults = new float[]{0f, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, 1f};
        int testCount = expectedResults.length;

        for (int i = 0; i < testCount; i++) {
            double progress = linearEasing.getProgress(i, testCount);
            assertThat(progress, closeTo(expectedResults[i], 0.01d));
        }
    }
}
