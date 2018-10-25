package io.sparkled.model.validator

import java.util.HashSet

object ValidatorUtils {

    fun <T> findDuplicates(objects: Collection<T>): Set<T> {
        val uniques = HashSet()
        val duplicates = HashSet()

        for (`object` in objects) {
            if (!uniques.add(`object`)) {
                duplicates.add(`object`)
            }
        }
        return duplicates
    }
}
