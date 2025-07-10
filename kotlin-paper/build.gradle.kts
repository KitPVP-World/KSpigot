plugins {
    alias(libs.plugins.kitpvp.project.paper)
    alias(libs.plugins.kitpvp.shadow)
    alias(libs.plugins.kitpvp.publish)
}

paperPluginYaml {
    main = "net.axay.kspigot.plugin.KSpigotPlugin"
    contributors = listOf("NotStevy")
}

dependencies {
    api(libs.commandapi.bukkit.shade)
    api(libs.commandapi.bukkit.kotlin)

    api(libs.kotlinx.coroutines)
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk9:${libs.versions.kotlinx.coroutines.get()}")
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.serialization.cbor)
    api("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin.get()}")
    api(libs.kotlinx.datetime)
}

publishing {
    repositories {
        maven("https://maven.kitpvp.world/public-snapshots/") {
            name = "kitpvpWorldRepository"
            credentials(PasswordCredentials::class)
        }
    }
}

tasks {
    shadowJar {
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }
}
