val googleGuavaVersion: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":persistence"))
    implementation(project(":music-player"))
    implementation("com.google.guava:guava:$googleGuavaVersion")
}
