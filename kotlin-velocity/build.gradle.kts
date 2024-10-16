plugins {
    alias(libs.plugins.kitpvp.project.velocity)
    alias(libs.plugins.kitpvp.shadow)
}

velocityPluginJson {
    main = "world.kitpvp.kotlin.KotlinVelocityPlugin"
    authors = listOf("NotStevy", "jakobkmar")
}

dependencies {
    api(libs.kotlinx.coroutines)
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk9:${libs.versions.kotlinx.coroutines.get()}")
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.serialization.cbor)
    api("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin.get()}")
    api(libs.kotlinx.datetime)
}