pluginManagement {
    repositories {
        mavenLocal()
        maven("https://maven.neuralnexus.dev/mirror")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

rootProject.name = "saddleup"

include(
    ":common",
    ":versions:v1_21_1"
)
