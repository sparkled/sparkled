package io.sparkled.persistence.util;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class PersistenceUtils {
    private PersistenceUtils() {
    }

    public static <T> Optional<T> getSingleResult(TypedQuery<T> query) {
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
