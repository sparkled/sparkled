val jdbiVersion: String by project

dependencies {
    implementation(project(":model"))
    implementation("com.madgag:animated-gif-lib:1.4")
    implementation("io.micronaut.sql:micronaut-jdbi")
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
