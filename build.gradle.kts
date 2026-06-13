plugins {
    java
    idea
    jacoco
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

}

allprojects {
    group = "org.isv.samples"

    repositories {
        // Use Maven Central for resolving dependencies.
        mavenCentral()
    }
}

subprojects {
    apply(plugin="idea")
    apply(plugin = "jacoco")
}
