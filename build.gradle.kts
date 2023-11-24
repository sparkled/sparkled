import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val projectVersion = "0.2.1"

plugins {
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.micronaut.aot)
    alias(libs.plugins.micronaut.application)
    alias(libs.plugins.shadow)

    // Plugins used by submodules.
    alias(libs.plugins.micronaut.library) apply false
}

val e2eTestImplementation: Configuration by configurations.creating {
    extendsFrom(configurations.testImplementation.get())
}

val e2eTestSourceSet: SourceSet = sourceSets.create("e2eTest") {
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

application {
    mainClass = "io.sparkled.Application"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

micronaut {
    runtime("netty")
    testRuntime("kotest5")
    version(libs.versions.micronaut.get())

    // Micronaut build-time optimisations to improve startup speed and reduce memory footprint.
    aot {
        cacheEnvironment = true
        convertYamlToJava = true
        deduceEnvironment = true
        optimizeClassLoading = true
        optimizeNetty = true
        optimizeServiceLoading = true
        precomputeOperations = true
    }

    processing {
        incremental(true)
        annotations("io.sparkled.*")
    }
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    compileTestKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    // Allow application version to be retrieved at runtime. Only works in full Gradle builds, not dev runs.
    jar {
        manifest {
            attributes["Implementation-Title"] = "Sparkled"
            attributes["Implementation-Version"] = project.version
        }
    }

    // Prevents the current build number from being appended to the shaded jar filename, which simplifies our deployment
    // logic as we don't need to account for the name changing.
    shadowJar {
        archiveVersion = ""
    }

    create<Test>("e2eTest") {
        group = "verification"
        testClassesDirs = e2eTestSourceSet.output.classesDirs
        classpath = e2eTestSourceSet.runtimeClasspath
    }
}

dependencies {
    e2eTestImplementation(project(":liquibase"))
    e2eTestImplementation(libs.jackson.kotlin)
    e2eTestImplementation(libs.jodd.http)
    e2eTestImplementation(libs.micronaut.data.jdbc)
    e2eTestImplementation(libs.micronaut.liquibase)
    e2eTestImplementation(libs.kotlin.reflect)

    kapt(libs.micronaut.data.processor)
    kapt(libs.micronaut.injectJava)

    implementation(project(":common"))
    implementation(project(":liquibase"))
    implementation(project(":model"))
    implementation(project(":persistence"))
    implementation(project(":renderer"))
    implementation(project(":api"))
    implementation(project(":scheduler"))
    implementation(project(":udp-server"))
    implementation(libs.kotlin.scriptingJsr223)
    implementation(libs.micronaut.data.jdbc)
    implementation(libs.micronaut.httpServerNetty)
    implementation(libs.micronaut.jacksonDatabind)
    implementation(libs.micronaut.kotlinRuntime)
    implementation(libs.micronaut.liquibase)
    implementation(libs.sqlite)

    runtimeOnly(libs.snakeyaml)

    scriptsImplementation(libs.jackson.kotlin)
    scriptsImplementation(libs.kopper)
    scriptsImplementation(libs.micronaut.data.jdbc)
    scriptsImplementation(libs.micronaut.liquibase)
    scriptsImplementation(libs.reflections)
}

allOpen {
    annotation("io.micronaut.aop.Around")
    annotation("io.micronaut.http.annotation.Controller")
    annotation("jakarta.inject.Singleton")
    annotation("jakarta.transaction.Transactional")
}

ktlint {
    version = "0.50.0"
    ignoreFailures = true
}

allprojects {
    group = "com.example"
    version = projectVersion

    repositories {
        mavenCentral()
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
