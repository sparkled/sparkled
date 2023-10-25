val jdbiVersion: String by project

dependencies {
    implementation("io.micronaut.data:micronaut-data-jdbc")
    compileOnly("jakarta.persistence:jakarta.persistence-api:3.1.0")

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
