plugins {
    id("java")
}

group = "ru.otus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
}

// В проекте 12 самостоятельных классов с методом main.
// Запуск конкретного примера:  ./gradlew runDemo -PmainClass=ru.otus.jmm.demo.CounterRace
tasks.register<JavaExec>("runDemo") {
    group = "application"
    description = "Запуск примера JMM. Класс задаётся через -PmainClass=<полное.имя.Класса>"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set(providers.gradleProperty("mainClass").orElse("ru.otus.jmm.demo.CounterRace"))
}

tasks.test {
    useJUnitPlatform()
}
