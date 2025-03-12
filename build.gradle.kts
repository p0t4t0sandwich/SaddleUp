import com.diffplug.gradle.spotless.SpotlessExtension
import java.time.Instant

plugins {
    id("java")
    id("maven-publish")
    id("idea")
    id("eclipse")
    alias(libs.plugins.spotless)
}

defaultTasks("build")

subprojects {
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "eclipse")
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    repositories {
        mavenLocal()
        maven("https://maven.neuralnexus.dev/releases")
        maven("https://maven.neuralnexus.dev/snapshots")
        maven("https://maven.neuralnexus.dev/mirror")
        flatDir {
            dirs("${rootProject.projectDir}/libs")
        }
    }

    dependencies {
        "compileOnly"("org.jetbrains:annotations:24.1.0")
    }

    spotless {
        format("misc") {
            target("*.gradle", ".gitattributes", ".gitignore")

            trimTrailingWhitespace()
            leadingTabsToSpaces()
            endWithNewline()
        }
        java {
            toggleOffOn()
            importOrder()
            removeUnusedImports()
            cleanthat()
            googleJavaFormat("1.24.0")
                .aosp()
                .formatJavadoc(true)
                .reorderImports(true)
            formatAnnotations()
            licenseHeader(
                """
            /**
             * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
             * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
             */
            """.trimIndent()
            )
        }
    }

    tasks.getByName("build").dependsOn("spotlessApply")
    tasks.findByName("shadowJar")?.dependsOn("spotlessApply")
    tasks.findByName("remapShadowJar")?.dependsOn("spotlessApply")
}

spotless {
    format("misc") {
        target("*.gradle", ".gitattributes", ".gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces()
        endWithNewline()
    }
    java {
        toggleOffOn()
        importOrder()
        removeUnusedImports()
        cleanthat()
        googleJavaFormat("1.24.0")
            .aosp()
            .formatJavadoc(true)
            .reorderImports(true)
        formatAnnotations()
        licenseHeader(
            """
            /**
             * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
             * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
             */
            """.trimIndent()
        )
    }
}

// --------------------------- Build MonoJar --------------------------------
tasks.register<Jar>("build_monojar") {
    val mc_version = "1.21.1"
    archiveFileName = "${projectId}-${version}.jar"
    destinationDirectory = file("./build/libs")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to "${projectName} ${mc_version}",
                "Specification-Version" to version,
                "Specification-Vendor" to "NeuralNexus",
                "Implementation-Version" to version,
                "Implementation-Vendor" to "NeuralNexus",
                "Implementation-Timestamp" to Instant.now().toString(),
                "FMLCorePluginContainsFMLMod" to "true",
                "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                "MixinConfigs" to "saddleup.mixins.json"
            )
        )
    }

    from(listOf("README.md", "LICENSE")) {
        into("META-INF")
    }
    val jarFiles = listOf(
            file("./versions/v1_21_1/build/libs/${projectId}-1.21.1-${version}.jar")
    )
    from(bundleJars(jarFiles))
}

tasks.withType<ProcessResources>().configureEach {
    filesMatching(
        listOf(
            "plugin.yml",
            "fabric.mod.json",
            "${projectId}.mixins.json",
            "META-INF/mods.toml",
            "META-INF/neoforge.mods.toml",
            "mcmod.info",
            "META-INF/sponge_plugins.json"
        )
    ) {
        expand(project.properties)
    }
}

