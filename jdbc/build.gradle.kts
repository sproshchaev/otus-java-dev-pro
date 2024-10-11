import org.gradle.kotlin.dsl.implementation

plugins {
    id("java")
    id("io.freefair.lombok") version "6.5.0.3" // Добавьте плагин Lombok
}

group = "com.prosoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hsqldb:hsqldb:2.7.3")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:5.0.1")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}