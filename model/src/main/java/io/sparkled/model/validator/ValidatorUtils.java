package io.sparkled.model.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ValidatorUtils {

    private ValidatorUtils() {
    }

    public static <T> Set<T> findDuplicates(Collection<T> objects) {
        final Set<T> uniques = new HashSet<>();
        final Set<T> duplicates = new HashSet<>();

        for (T object : objects) {
            if (!uniques.add(object)) {
                duplicates.add(object);
            }
        }
        return duplicates;
    }
}
