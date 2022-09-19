import com.moowork.gradle.node.npm.NpmTask
plugins {
  id("com.moowork.node") version "1.3.1"
}

tasks {
  create<Delete>("deleteWebUi") {
    delete("$rootDir/src/main/resources/webui")
    dependsOn("npmSetup")
  }

  create<NpmTask>("buildWebUi") {
    setArgs(listOf("run", "build"))
    dependsOn("npmInstall")
  }

  create<Copy>("copyWebUi") {
    from(buildDir)
    into("$rootDir/src/main/resources/webui")
    dependsOn("deleteWebUi")
  }

  findByName("npmInstall")?.dependsOn("deleteWebUi")
}
