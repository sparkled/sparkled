package io.sparkled.persistence;

import javax.persistence.EntityManager;

public interface PersistenceQuery<T> {

    T perform(EntityManager entityManager);
}
