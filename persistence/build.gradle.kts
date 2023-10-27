dependencies {
    implementation(project(":model"))
    implementation("com.madgag:animated-gif-lib:1.4")

    kapt("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut.data:micronaut-data-jdbc")
}
