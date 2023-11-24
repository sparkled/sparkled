plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.micronaut.library)
}

dependencies {
    compileOnly(libs.jakarta.persistenceApi)

    kapt(libs.micronaut.data.processor)
    kapt(libs.micronaut.injectJava)

    implementation(project(":common"))
    implementation(libs.jackson.kotlin)
    implementation(libs.jakarta.annotationApi)
    implementation(libs.micronaut.data.jdbc)

    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.runner)
}
