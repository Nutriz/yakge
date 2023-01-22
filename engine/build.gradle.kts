plugins {
    kotlin("jvm") version "1.8.0"
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

group = "fr.nutriz.engine"
version = "0.1-alpha1"

val lwjglVersion = "3.3.1"
val jomlVersion = "1.10.5"

val lwjglNatives = when (org.gradle.internal.os.OperatingSystem.current()) {
    org.gradle.internal.os.OperatingSystem.LINUX   -> "natives-linux"
    org.gradle.internal.os.OperatingSystem.MAC_OS  -> "natives-macos"
    org.gradle.internal.os.OperatingSystem.WINDOWS -> "natives-windows"
    else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

repositories {
    mavenCentral()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

dependencies {
    api("dev.romainguy:kotlin-math:1.5.3")
    api("org.joml", "joml", jomlVersion)

    api(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    api("org.lwjgl", "lwjgl")
    api("org.lwjgl", "lwjgl-assimp")
    api("org.lwjgl", "lwjgl-glfw")
    api("org.lwjgl", "lwjgl-nfd")
    api("org.lwjgl", "lwjgl-nanovg")
    api("org.lwjgl", "lwjgl-nuklear")
    api("org.lwjgl", "lwjgl-openal")
    api("org.lwjgl", "lwjgl-opengl")
    api("org.lwjgl", "lwjgl-par")
    api("org.lwjgl", "lwjgl-stb")
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
    testImplementation(kotlin("test-junit"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    config = files("${rootProject.projectDir}/config/detekt-config.yml")
}