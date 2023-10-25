plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.micronaut.library)
}

dependencies {
    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.runner)
}
