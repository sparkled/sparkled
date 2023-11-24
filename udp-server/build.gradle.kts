plugins {
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.micronaut.library)
}

allOpen {
    annotation("jakarta.inject.Singleton")
    annotation("io.micronaut.transaction.annotation.Transactional")
}

dependencies {
    kapt(libs.micronaut.data.processor)
    kapt(libs.micronaut.injectJava)

    implementation(project(":common"))
    implementation(project(":model"))
    implementation(project(":persistence"))
    implementation(project(":music-player"))
    implementation(libs.guava)
    implementation(libs.kotlin.coroutines)
    implementation(libs.micronaut.data.jdbc)
    implementation(libs.micronaut.injectJava)
    implementation(libs.micronaut.kotlinRuntime)

    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.runner)
}
