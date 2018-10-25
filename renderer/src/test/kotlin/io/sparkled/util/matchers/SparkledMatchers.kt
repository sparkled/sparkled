package io.sparkled.util.matchers

import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import org.hamcrest.Matcher

object SparkledMatchers {
    fun hasLeds(leds: Array<IntArray>): Matcher<RenderedStagePropData> {
        return RenderedStagePropDataLedMatcher(leds)
    }

    fun hasLeds(leds: IntArray): Matcher<RenderedFrame> {
        return RenderedFrameLedMatcher(leds)
    }
}
