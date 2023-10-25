package io.sparkled

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .mainClass(this.javaClass)
            .start()
    }
}
