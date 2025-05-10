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

    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("io.insert-koin:koin-annotations:2.0.0")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation ("com.google.truth:truth:1.4.4")
    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("com.soywiz.korlibs.krypto:krypto:3.4.0")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    implementation("org.slf4j:slf4j-simple:2.0.12")
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
    "**/presentation/UIFeature.kt",
    "**/presentation/UiLauncher.kt",
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
                minimum = "0.8".toBigDecimal()
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
