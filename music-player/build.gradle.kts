val mp3SpiVersion: String by project
val googleGuavaVersion: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":persistence"))
    implementation("com.googlecode.soundlibs:mp3spi:$mp3SpiVersion")
    implementation("com.google.guava:guava:$googleGuavaVersion")
    implementation("io.micronaut.data:micronaut-data-jdbc")
}
