package io.sparkled.model.util


import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.sparkled.model.util.IdUtils.uniqueId
import kotlin.system.measureTimeMillis

class IdUtilsTest : StringSpec() {

    init {
        "can generate random identifiers" {
            measureTimeMillis {
                repeat(1000) {
                    val id = uniqueId()
                    id.length shouldBe 12
                }
            } shouldBeLessThan 100
        }
    }
}
