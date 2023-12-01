import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

val githubRepo = "KitPVP-World/KSpigot"

plugins {
    kotlin("plugin.serialization") version "1.9.21"
    kotlin("jvm") version "1.9.21"

    `java-library`
    `maven-publish`
    signing

    id("org.jetbrains.dokka") version "1.9.10"

    id("com.github.johnrengelman.shadow") version "8.1.1" // Using shadow because "java.lang.LinkageError: loader constraint" violation when multiple plugins depend on kotlin

    id("io.papermc.paperweight.userdev") version "1.5.10"
}

group = "world.kitpvp"
version = "1.20.2+1.9.21"
description = "A Kotlin API for Minecraft plugins using the Paper toolchain"

repositories {
    mavenCentral()
    maven("https://repo.codemc.org/repository/maven-public/")
}


dependencies {
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")
    api("org.jetbrains.kotlin:kotlin-reflect:1.9.21")

    api("dev.jorel:commandapi-bukkit-shade:9.2.0")
    api("dev.jorel:commandapi-bukkit-kotlin:9.2.0")
}

tasks {
    assemble {
        dependsOn(shadowJar)
        dependsOn(reobfJar)
    }

    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
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
            "apiVersion" to "1.20"
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

    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
            credentials {
                username = System.getenv("MVNREPO_UB_USERNAME")
                password = System.getenv("MVNREPO_UB_PASSWORD")
            }
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
