plugins {
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.micronaut.library)
}

allOpen {
    annotation("jakarta.inject.Singleton")
    annotation("jakarta.transaction.Transactional")
}

dependencies {
    kapt(libs.micronaut.injectJava)

    implementation(project(":common"))
    implementation(project(":model"))
    implementation(project(":persistence"))
    implementation(libs.guava)
    implementation(libs.jackson.kotlin)
    implementation(libs.jakarta.annotationApi)
    implementation(libs.micronaut.data.jdbc)
    implementation(libs.mp3Spi)

    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.runner)
}
