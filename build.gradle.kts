
repositories {
    mavenCentral()
}

// fat jar to use with RenderDoc
//tasks.withType<Jar>  {
//    manifest {
//        attributes["Main-Class"] = "MainKt"
//    }
//    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//        .exclude("**/module-info.class")
//        .exclude("**/INDEX.LIST")
//}