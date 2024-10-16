plugins {
    alias(libs.plugins.kitpvp.project.paper)
    alias(libs.plugins.kitpvp.shadow)
    alias(libs.plugins.kitpvp.publish)
}

dependencies {
    paperweight.devBundle("world.kitpvp.kitpvpslime", "1.21.1-R0.1-SNAPSHOT")

    api("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.5.3") // https://github.com/JorelAli/CommandAPI/releases/latest
    api("dev.jorel:commandapi-bukkit-kotlin:9.5.3")
}

paperPluginYaml {
    main = "net.axay.kspigot.plugin.KSpigotPlugin"
    contributors = listOf("NotStevy")
}

dependencies {
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
            name = "ultrabuildRepository"
            credentials(PasswordCredentials::class)
        }
    }
}

tasks {
    shadowJar {
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }
}