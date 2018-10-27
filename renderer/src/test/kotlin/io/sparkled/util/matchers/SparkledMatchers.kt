package io.sparkled.util.matchers

import io.sparkled.model.render.RenderedStagePropData
import org.hamcrest.Matcher

object SparkledMatchers {
    fun hasRenderedFrames(vararg leds: IntArray): Matcher<RenderedStagePropData> {
        return RenderedStagePropDataLedMatcher(arrayOf(*leds))
    }
}
