import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

val githubRepo = "KitPVP-World/KSpigot"

plugins {
    kotlin("plugin.serialization") version "2.0.0"
    kotlin("jvm") version "2.0.0"

    `maven-publish`
    signing

    // https://github.com/Kotlin/dokka/releases/latest
    id("org.jetbrains.dokka") version "1.9.20"

    // https://github.com/johnrengelman/shadow/releases/latest
    id("com.github.johnrengelman.shadow") version "8.1.1" // Using shadow because "java.lang.LinkageError: loader constraint" violation when multiple plugins depend on kotlin

    // https://github.com/PaperMC/paperweight/releases/latest
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "world.kitpvp"
version = "1.21+2.0.0"
description = "A Kotlin API for Minecraft plugins using the Paper toolchain"

repositories {
    mavenCentral()
    maven("https://repo.codemc.org/repository/maven-public/")
}


dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC") // https://github.com/Kotlin/kotlinx.coroutines/releases/latest
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1") // https://github.com/Kotlin/kotlinx.serialization/releases/latest
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.9.0-RC")
    api("org.jetbrains.kotlin:kotlin-reflect:2.0.0")

    api("dev.jorel:commandapi-bukkit-shade:9.5.1") // https://github.com/JorelAli/CommandAPI/releases/latest
    api("dev.jorel:commandapi-bukkit-kotlin:9.5.1")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

kotlin {
    jvmToolchain(21)
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    dokkaHtml.configure {
        outputDirectory.set(projectDir.resolve("docs"))
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.21"
        )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }


}

java {
    withSourcesJar()
    withJavadocJar()
}

/*
signing {
    sign(publishing.publications)
}
*/
publishing {
    repositories {
        maven("https://maven.kitpvp.world/public-snapshots/") {
            name = "ultrabuild-public-snapshots"
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        register<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks.jar.get().outputs.files.single())

            this.groupId = project.group.toString()
            this.artifactId = project.name.lowercase(Locale.getDefault())
            this.version = project.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)

                developers {
                    developer {
                        name.set("jakobkmar")
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License, Version 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                    }
                }

                url.set("https://github.com/${githubRepo}")

                scm {
                    connection.set("scm:git:git://github.com/${githubRepo}.git")
                    url.set("https://github.com/${githubRepo}/tree/main")
                }
            }
        }
    }
}
