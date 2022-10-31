val jdbiVersion: String by project

dependencies {
    implementation("org.jdbi:jdbi3-core:$jdbiVersion") {
        exclude(group = "com.github.ben-manes.caffeine", module = "caffeine")
    }
    implementation("org.jdbi:jdbi3-kotlin:$jdbiVersion") {
        exclude(group = "com.github.ben-manes.caffeine", module = "caffeine")
    }
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:$jdbiVersion") {
        exclude(group = "com.github.ben-manes.caffeine", module = "caffeine")
    }
}
