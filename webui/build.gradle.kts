import com.github.gradle.node.npm.task.NpmTask

plugins {
    alias(libs.plugins.node)
}

tasks {
    create<NpmTask>("buildWebUi") {
        args = listOf("run", "build")
    }

    create<Copy>("copyWebUi") {
        from("${rootDir}/webUi/dist")
        into("$rootDir/src/main/resources/webui")
    }
}
