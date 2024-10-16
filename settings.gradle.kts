pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.kitpvp.world/snapshots/") {
            name = "ultrabuildRepository"
            credentials(PasswordCredentials::class)
        }
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://maven.kitpvp.world/public-releases/") {
            name = "ultrabuildRepository"
            credentials(PasswordCredentials::class)
        }
    }

    versionCatalogs {
        create("libs") {
            from("world.kitpvp:version-cataloge:1.4-alpha.13")
        }
    }
}

rootProject.name = "kitpvp-kotlin"

include("kotlin-velocity", "kotlin-paper")