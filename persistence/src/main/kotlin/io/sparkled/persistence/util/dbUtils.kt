package io.sparkled.persistence.util

import java.util.*

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

fun toSnakeCase(s: String): String {
    return camelRegex.replace(s) {
        "_${it.value}"
    }.lowercase(Locale.getDefault())
}

