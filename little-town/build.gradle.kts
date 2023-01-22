import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
}

group = "fr.nutriz.littletown"
version = "0.1-alpha1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":engine"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
