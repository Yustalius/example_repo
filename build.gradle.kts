plugins {
    java
    id("io.qameta.allure") version "3.0.1"
    id("io.freefair.lombok") version "9.5.0"
}

val allureVersion = "2.32.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("com.codeborne:selenide:7.17.0")
    testImplementation("io.rest-assured:rest-assured:6.0.0")
    testImplementation("tools.jackson.core:jackson-databind:3.2.2")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.aeonbits.owner:owner:1.0.12")
    testImplementation("net.datafaker:datafaker:2.7.0")

    testImplementation("io.qameta.allure:allure-junit5:$allureVersion")
    testImplementation("io.qameta.allure:allure-selenide:$allureVersion")
    testImplementation("io.qameta.allure:allure-rest-assured:$allureVersion")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

allure {
    report {
        version = allureVersion
    }
}

val forwardedProperties = listOf(
    "run", "browser", "browserVersion", "browserSize",
    "remoteUrl", "remoteLogin", "remotePassword", "videoStorageUrl",
    "bookingUrl", "bookingUsername", "bookingPassword"
)

tasks.withType<Test>().configureEach {
    jvmArgs("-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8")
    forwardedProperties.forEach { key ->
        System.getProperty(key)?.let { systemProperty(key, it) }
    }

    testLogging {
        events("passed", "failed", "skipped")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.test {
    useJUnitPlatform()
}

fun Test.configureTaggedRun(layerTag: String) {
    group = "verification"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath

    val tag = System.getProperty("tag")
    useJUnitPlatform {
        includeTags(if (tag.isNullOrBlank()) layerTag else "$layerTag & $tag")
    }
}

tasks.register<Test>("uiTest") {
    description = "UI-тесты demowebshop"
    configureTaggedRun("UI")
}

tasks.register<Test>("apiTest") {
    description = "API-тесты restful-booker"
    configureTaggedRun("API")
}
