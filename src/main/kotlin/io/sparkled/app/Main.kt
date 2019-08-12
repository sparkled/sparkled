package io.sparkled.app

import io.micronaut.runtime.Micronaut

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .mainClass(this.javaClass)
            .start()
    }
}
