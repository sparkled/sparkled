package io.sparkled.model.animation

import java.awt.Color

class Colors : ArrayList<Color> {
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor(c: Collection<Color>) : super(c)
    constructor(vararg c: Color) : super(c.toList())
}

fun colorListOf(vararg colors: String) = colors.mapTo(Colors(colors.size)) { Color.decode(it.lowercase())}