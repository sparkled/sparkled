package io.sparkled.util.matchers

/**
 * Convenience matcher for comparing arrays of floats.
 */
// TODO
//class FloatArrayMatcher(private val values: FloatArray) : TypeSafeMatcher<FloatArray>() {
//
//    override fun describeTo(description: Description) {
//        val value = floatArrayToString(values)
//        description.appendText("is ").appendValue(value)
//    }
//
//    override fun describeMismatchSafely(item: FloatArray, mismatchDescription: Description) {
//        val value = floatArrayToString(item)
//        mismatchDescription.appendText("was ").appendValue(value)
//    }
//
//    public override fun matchesSafely(item: FloatArray): Boolean {
//        return floatArrayToString(item) == floatArrayToString(values)
//    }
//
//    private fun floatArrayToString(array: FloatArray): String {
//        return array.joinToString(", ") { "%.3f".format(it) }
//    }
//}
