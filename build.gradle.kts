plugins {
    kotlin("jvm") version "1.9.24"
    `maven-publish`
    application
}
group = "com.github.simonlucas"
version = "1.0.0"
repositories { mavenCentral() }
kotlin { jvmToolchain(17) }
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = group.toString()
            artifactId = "kplotlib"
            version = version
        }
    }
}

// Task to run the random plot generator
tasks.register<JavaExec>("randomPlot") {
    group = "application"
    description = "Generate a random plot (for README interactive example)"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("sml.plotlib.examples.RandomPlotGeneratorKt")
}
