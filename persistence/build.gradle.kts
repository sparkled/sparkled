plugins {
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.micronaut.library)
}

allOpen {
    annotation("io.micronaut.data.jdbc.annotation.JdbcRepository")
    annotation("jakarta.inject.Singleton")
    annotation("io.micronaut.transaction.annotation.Transactional")
}

dependencies {
    kapt(libs.micronaut.data.processor)
    kapt(libs.micronaut.injectJava)

    implementation(project(":common"))
    implementation(project(":model"))
    implementation(libs.jackson.kotlin)
    implementation(libs.jakarta.annotationApi)
    implementation(libs.kotlin.reflect)
    implementation(libs.micronaut.injectJava)
    implementation(libs.micronaut.data.jdbc)
    implementation(libs.madgag.animatedGifLib)

    runtimeOnly(libs.micronaut.jdbc.hikari)

    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.runner)
}
