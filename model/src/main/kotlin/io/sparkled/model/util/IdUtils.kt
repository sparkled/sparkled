package io.sparkled.model.util

import kotlin.random.Random

object IdUtils {

    /**
     * @return A unique 12-digit identifier using time and randomness.
     */
    fun uniqueId(): String {
        val ms = System.currentTimeMillis() * RANDOM_FACTOR
        val rand = Random.nextInt(0, RANDOM_FACTOR)

        return (ms + rand)
            .toString(radix = ID_REMAP_TABLE.size)
            .map { ID_REMAP_TABLE[it] ?: it }
            .joinToString("")
    }

    /**
     * Multiplying the current millis by this factor will yield a 12-digit base 28 identifier. We can then add a random
     * number less than this factor to the timestamp to reduce the possibility of a collision.
     */
    private const val RANDOM_FACTOR = 70000

    private val ID_REMAP_TABLE = mapOf(
        '0' to '2',
        '1' to '3',
        '2' to '4',
        '3' to '5',
        '4' to '6',
        '5' to '7',
        '6' to '8',
        '7' to '9',
        '8' to 'b',
        '9' to 'c',
        'a' to 'd',
        'b' to 'f',
        'c' to 'g',
        'd' to 'h',
        'e' to 'j',
        'f' to 'k',
        'g' to 'm',
        'h' to 'n',
        'i' to 'p',
        'j' to 'q',
        'k' to 'r',
        'l' to 's',
        'm' to 't',
        'n' to 'v',
        'o' to 'w',
        'p' to 'x',
        'q' to 'y',
        'r' to 'z',
    )
}
