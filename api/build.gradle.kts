dependencies {
    implementation(project(":common"))
    implementation(project(":model"))
    implementation(project(":music-player"))
    implementation(project(":persistence"))
    implementation(project(":renderer"))
    implementation(project(":scheduler"))

    implementation("io.micronaut.data:micronaut-data-jdbc")
}
