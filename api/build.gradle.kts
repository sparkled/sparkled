plugins {
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.micronaut.library)
}

allOpen {
    annotation("io.micronaut.http.annotation.Controller")
    annotation("io.micronaut.runtime.http.scope.RequestScope")
    annotation("io.micronaut.transaction.annotation.Transactional")
}

dependencies {
    compileOnly(libs.jakarta.persistenceApi)

    kapt(libs.micronaut.openapi)
    kapt(libs.micronaut.security)

    implementation(project(":common"))
    implementation(project(":model"))
    implementation(project(":music-player"))
    implementation(project(":persistence"))
    implementation(project(":renderer"))
    implementation(project(":scheduler"))
    implementation(libs.jackson.kotlin)
    implementation(libs.micronaut.data.jdbc)
    implementation(libs.micronaut.injectJava)
    implementation(libs.micronaut.httpServerNetty)
    implementation(libs.micronaut.security)
    implementation(libs.micronaut.websocket)
    implementation(libs.swagger.annotations)

    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.runner)
}
