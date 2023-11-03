import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

val projectVersion = "0.2.1"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.kotlin.kapt") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.4.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

val javaNativeAccessVersion: String by project
val jetbrainsAnnotationsVersion: String by project
val joddHttpVersion: String by project
val kopperVersion: String by project
val kotestVersion: String by project
val kotlinCoroutinesVersion: String by project
val kotlinRetryVersion: String by project
val kotlinVersion: String by project
val logbackJacksonVersion: String by project
val logbackJsonClassicVersion: String by project
val micronautVersion: String by project
val micronautOpenApiVersion: String by project
val mockkVersion: String by project
val sqliteVersion: String by project

application {
    mainClass.set("io.sparkled.app.Main")
}

java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

micronaut {
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("io.sparkled.*")
    }
}

val e2eTestImplementation: Configuration by configurations.creating {
    extendsFrom(configurations.testImplementation.get())
}

val e2eTestRuntime: Configuration by configurations.creating {
    extendsFrom(configurations.testRuntimeClasspath.get())
}

val e2eTestSourceSet = sourceSets.create("e2eTest") {
    java.srcDir("src/e2e-test/kotlin")
    resources.srcDir("src/e2e-test/resources")
    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
}

val scriptsImplementation: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
}

val scriptsSourceSet: SourceSet = sourceSets.create("scripts") {
    java.srcDir("src/scripts/kotlin")
    resources.srcDir("src/scripts/resources")
    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
}

tasks {
    withType<ShadowJar> {
        archiveVersion.set("")
    }

    create<Test>("e2eTest") {
        group = "verification"
        testClassesDirs = e2eTestSourceSet.output.classesDirs
        classpath = e2eTestSourceSet.runtimeClasspath
    }
}

dependencies {
    implementation(project(":liquibase"))
    implementation(project(":model"))
    implementation(project(":persistence")) // TODO rename to db.
    implementation(project(":renderer"))
    implementation(project(":api"))
    implementation(project(":scheduler"))
    implementation(project(":udp-server"))

    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.liquibase:micronaut-liquibase")
    implementation("io.micronaut:micronaut-runtime")

    e2eTestImplementation(project(":liquibase"))
    e2eTestImplementation("io.micronaut.liquibase:micronaut-liquibase")
    e2eTestImplementation("io.micronaut.data:micronaut-data-jdbc")
    e2eTestImplementation("org.jodd:jodd-http:$joddHttpVersion")

    scriptsImplementation("org.jodd:jodd-http:$joddHttpVersion")
}

allprojects {
    version = projectVersion
    group = "io.sparkled"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ext {
        set("dbServer", System.getProperty("db.server", "dbServer"))
        set("dbName", System.getProperty("db.name", "dbName"))
        set("dbUsername", System.getProperty("db.username", "dbUsername"))
        set("dbPassword", System.getProperty("db.password", "dbPassword"))
    }

    kapt {
        arguments {
            arg("micronaut.processing.incremental", true)
            arg("micronaut.processing.annotations", "io.sparkled.*")
        }
    }

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configure<KtlintExtension> {
        ignoreFailures.set(true)
        disabledRules.add("no-wildcard-imports")
    }

    dependencies {
        // Prevent warning as per https://docs.micronaut.io/latest/guide/index.html#_nullable_annotations.
        compileOnly("com.google.code.findbugs:jsr305")
        compileOnly("org.jetbrains:annotations:$jetbrainsAnnotationsVersion")

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
        implementation("io.micronaut.security:micronaut-security")
        implementation("io.micronaut:micronaut-tracing") // Required for the LoggingFilter, see comment on that class.
        implementation("io.micronaut:micronaut-runtime")
        implementation("org.springframework:spring-jdbc")
        implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
        implementation("io.swagger.core.v3:swagger-annotations")
        implementation("io.projectreactor:reactor-core")
        implementation("jakarta.annotation:jakarta.annotation-api")
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
        implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))

        // Kotlin runtime scripting.
        implementation("org.jetbrains.kotlin:kotlin-script-runtime")
        implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
        implementation("org.jetbrains.kotlin:kotlin-script-util:1.8.22")
        implementation("net.java.dev.jna:jna:$javaNativeAccessVersion")
        runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable")

        runtimeOnly("ch.qos.logback:logback-classic")
        runtimeOnly("ch.qos.logback.contrib:logback-json-classic:$logbackJsonClassicVersion")
        runtimeOnly("ch.qos.logback.contrib:logback-jackson:$logbackJacksonVersion")

        kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
        kapt("io.micronaut:micronaut-inject-java")
        kapt("io.micronaut.openapi:micronaut-openapi:$micronautOpenApiVersion")
        kapt("io.micronaut.security:micronaut-security")
        kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
        kaptTest("io.micronaut:micronaut-inject-java")

        testImplementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
        testImplementation("io.mockk:mockk:$mockkVersion")
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.kotest:kotest-framework-concurrency:$kotestVersion")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "17"
                javaParameters = true
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }

        // Allow application version to be retrieved at runtime. Only works in full Gradle builds, not dev runs.
        withType<Jar> {
            manifest {
                attributes["Implementation-Title"] = "Sparkled API"
                attributes["Implementation-Version"] = project.version
            }
        }
    }

    // Micronaut requires that Kotlin classes be marked as "open" in order to perform AOP interception.
    // We leverage this for transactions using the @Transactional annotation in our Controller classes and
    // some services. The Kotlin all-open plugin automatically applies the "open" keyword at compile time,
    // instead of us needing to remember to add it. Classes annotated with one of the below are affected.
    allOpen {
        annotation("io.micronaut.aop.Around")
        annotation("io.micronaut.http.annotation.Controller")
        annotation("jakarta.inject.Singleton")
        annotation("javax.transaction.Transactional")

        // TODO remove once migrated to Micronaut Data.
        annotation("org.springframework.transaction.annotation.Transactional")
    }
}
