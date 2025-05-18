plugins {
    kotlin("jvm") version "2.1.20"
    id("jacoco")
}

group = "squad.abudhabi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    // Kotlin Test Framework
    testImplementation(kotlin("test"))

    // Dependency Injection (Koin)
    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("io.insert-koin:koin-annotations:2.0.0")

    // Coroutine Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // Assertion and Mocking Libraries
    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("io.mockk:mockk:1.14.0")

    // Unit Testing Framework
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // MongoDB Kotlin Coroutine Driver
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")

    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.12")

    // Date and Time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

val includedPackages = listOf(
    "**/data/**",
    "**/logic/**",
    "**/presentation/**"
)

val excludedPackages = listOf(
    "**/di/**",
    "**/data/utils/**",
    "**/data/**/**/mongo/**",
    "**/data/**/model/**",
    "**/logic/model/**",
    "**/logic/exceptions/**",
    "**/presentation/io/**",
    "**/presentation/UIFeature*",
    "**/presentation/utils/**",
)

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
            include(includedPackages)
            exclude(excludedPackages)
        }
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }

    classDirectories.setFrom(
        fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
            include(includedPackages)
            exclude(excludedPackages)
        }
    )
}
