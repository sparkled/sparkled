package io.sparkled.model.util;

import com.querydsl.core.Tuple;

/**
 * Helper functions for tuples.
 */
public class TupleUtils {

    private TupleUtils() {
    }

    /**
     * Retrieves an integer from a tuple. Importantly, this method works when the raw tuple value is a Long.
     *
     * @param tuple The tuple from which to extract an integer value.
     * @param index The tuple index (0-based).
     * @return The tuple value as an integer, or 0 if no value exists.
     */
    public static Integer getInt(Tuple tuple, int index) {
        Number value = tuple.get(index, Number.class);
        return value == null ? 0 : value.intValue();
    }
}
