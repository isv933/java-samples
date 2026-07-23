plugins {
    id("org.springframework.boot") version "3.5.15"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.postgresql:postgresql")
    implementation("com.clickhouse:clickhouse-jdbc:0.9.8:all")
    implementation("io.github.pelenthium:clickhouse-dialect-spring-boot-starter:1.2.0")
    implementation("org.apache.kafka:kafka-streams")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.apache.kafka:kafka-streams-test-utils")

}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("load")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.register<Test>("loadTest") {
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    useJUnitPlatform {
        includeTags("load")
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}
