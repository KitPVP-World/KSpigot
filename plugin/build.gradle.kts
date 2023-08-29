import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1" // Using shadow because loading libraries doesn't work well
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.1")
}

tasks {
    withType<Jar> {
        archiveBaseName.set("kotlin-language")
        archiveClassifier.set("") // To Disable default jar generation and remove confusion
    }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("kotlin-language")
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}
