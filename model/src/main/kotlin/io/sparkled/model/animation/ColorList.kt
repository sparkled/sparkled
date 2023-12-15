package io.sparkled.model.animation

import java.awt.Color

class ColorList : ArrayList<Color> {
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor(c: Collection<Color>) : super(c)
}