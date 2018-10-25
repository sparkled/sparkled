package io.sparkled.model.validator

import java.util.*

object ValidatorUtils {

    fun <T> findDuplicates(objects: Collection<T>): Set<T> {
        val uniques = HashSet<T>()
        return objects
                .filterNot { uniques.add(it) }
                .toSet()
    }
}
