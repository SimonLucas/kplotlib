plugins {
    kotlin("jvm") version "1.9.24"
    `maven-publish`
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
