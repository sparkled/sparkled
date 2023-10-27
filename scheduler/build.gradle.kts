val quartzVersion: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":persistence"))
    implementation(project(":music-player"))
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("org.quartz-scheduler:quartz:$quartzVersion")
}
