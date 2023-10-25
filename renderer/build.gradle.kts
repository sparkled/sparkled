plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.micronaut.library)
}

dependencies {
    kapt(libs.micronaut.injectJava)

    implementation(project(":common"))
    implementation(project(":model"))
    implementation(libs.jackson.kotlin)
    implementation(libs.micronaut.injectJava)
    implementation(libs.micronaut.kotlinRuntime)

    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.runner)
}
