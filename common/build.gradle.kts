import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.shadow)
}

base {
    archivesName = "${projectId}-common"
}

dependencies {
    // Configurate
    implementation("org.spongepowered:configurate-hocon:4.2.0")
    implementation("io.leangen.geantyref:geantyref:1.3.15")

    // bStats
    implementation("org.bstats:bstats-base:3.0.2")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("org.bstats:bstats-bungeecord:3.0.2")
    implementation("org.bstats:bstats-sponge:3.0.2")
    implementation("org.bstats:bstats-velocity:3.0.2")

    // Mixin
    compileOnly(libs.mixin)
    compileOnly(libs.asm.tree)

    implementation(variantOf(libs.modapi) {
        classifier("downgraded-8-shaded")
    })
}

java.disableAutoTargetJvm()

tasks.shadowJar {
    dependencies {
    }
    // Configurate
    relocate("org.spongepowered.configurate", "dev.neuralnexus.saddleup.lib.configurate")
    relocate("com.typesafe.config", "dev.neuralnexus.saddleup.lib.typesafe.config")
    relocate("io.leangen.geantyref", "dev.neuralnexus.saddleup.lib.geantyref")

    // bStats
    relocate("org.bstats", "dev.neuralnexus.saddleup.lib.bstats") {
        exclude("org.bstats.sponge.*")
    }

    // minimize {}

    archiveClassifier.set("full")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
