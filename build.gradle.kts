import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

val githubRepo = "KitPVP-World/KSpigot"

plugins {
    kotlin("plugin.serialization") version "1.8.22"
    kotlin("jvm") version "1.8.22"

    `java-library`
    `maven-publish`
    signing

    id("io.papermc.paperweight.userdev") version "1.5.5"
}

allprojects {
    group = "world.kitpvp"
    version = "1.20.1+1.8.22"

    description = "A Kotlin API for Minecraft plugins using the Paper toolchain"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply<JavaPlugin>()

    repositories {
        mavenCentral()
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(17)
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }
    }

    java {
        withSourcesJar()
        withJavadocJar()
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.1")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}

/*
signing {
    sign(publishing.publications)
}
*/
publishing {
    repositories {
        maven("https://maven.ultrabuildmc.de/public-snapshots/") {
            name = "public-snapshots"
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
