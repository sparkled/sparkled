package io.sparkled.renderer.api

import io.sparkled.model.animation.param.Param

interface SparkledPlugin {
    val id: String

    val name: String

    val params: List<Param>
        get() = emptyList()
}
