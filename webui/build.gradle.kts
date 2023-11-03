import com.moowork.gradle.node.npm.NpmTask

plugins {
  id("com.moowork.node") version "1.3.1"
}

tasks {
  create<Delete>("deleteWebUi") {
    dependsOn("npmSetup")

    delete("$rootDir/src/main/resources/webui")
  }

  create<NpmTask>("buildWebUi") {
    dependsOn("npmInstall")

    setArgs(listOf("run", "build"))
  }

  create<Copy>("copyWebUi") {
    dependsOn("deleteWebUi")

    from(buildDir)
    into("$rootDir/src/main/resources/webui")
  }

  findByName("npmInstall")?.dependsOn("deleteWebUi")
}
