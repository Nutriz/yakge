import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "fr.nutriz"
version = "0.1-alpha1"
val lwjglVersion = "3.3.1"
val jomlVersion = "1.10.3"

val lwjglNatives = when (OperatingSystem.current()) {
    OperatingSystem.LINUX   -> "natives-linux"
    OperatingSystem.MAC_OS  -> "natives-macos"
    OperatingSystem.WINDOWS -> "natives-windows"
    else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("dev.romainguy:kotlin-math:1.2.0")

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation("org.joml", "joml", jomlVersion)
    testImplementation(kotlin("test-junit"))
}

// fat jar to use with RenderDoc
tasks.withType<Jar>  {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        .exclude("**/module-info.class")
        .exclude("**/INDEX.LIST")
}