import com.github.gradle.node.npm.task.NodeTask

plugins {
  alias(libs.plugins.node)
}

repositories {
  gradlePluginPortal()
}

tasks {
  create<Delete>("deleteWebUi") {
    dependsOn("npmSetup")

    delete("$rootDir/src/main/resources/webui")
  }

  create<NodeTask>("buildWebUi") {
    dependsOn("npmInstall")

    setArgs(listOf("run", "build"))
  }

  create<Copy>("copyWebUi") {
    dependsOn("deleteWebUi")

    from(layout.buildDirectory)
    into("$rootDir/src/main/resources/webui")
  }

  findByName("npmInstall")?.dependsOn("deleteWebUi")
}
