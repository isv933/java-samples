plugins {
    idea
    jacoco
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
    apply(plugin = "java")
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}
