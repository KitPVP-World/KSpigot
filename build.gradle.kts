plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.kitpvp.gitversioning)
    alias(libs.plugins.kitpvp.shadow) apply false
    alias(libs.plugins.kitpvp.project.kotlin) apply false
    alias(libs.plugins.kitpvp.project.paper) apply false
    alias(libs.plugins.kitpvp.project.velocity) apply false
    alias(libs.plugins.kitpvp.publish) apply false
//    alias(libs.plugins.kitpvp.dokka.single) apply false
}

allprojects {
    group = "world.kitpvp"
    description = "A Kotlin API for Minecraft plugins using the Paper toolchain"
}

subprojects {
    version = rootProject.version

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "com.gradleup.shadow")

    val authors: List<String> by extra(listOf("jakobkmar", "notstevy"))

//
//    tasks {
//        dokkaHtml.configure {
//            outputDirectory.set(projectDir.resolve("docs"))
//        }
//    }
}
