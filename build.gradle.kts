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
    testImplementation ("com.google.truth:truth:1.4.4")
    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

val includedPackages = listOf(
    "squad/abudhabi/data",
    "squad/abudhabi/logic",
)

val excludedPackages = listOf(
    "squad/abudhabi/data/utils/**"
)

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(
            fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
                include(includedPackages.map { "$it/**" })
                exclude(excludedPackages)
            },
            fileTree(layout.buildDirectory.dir("classes/java/main")) {
                include(includedPackages.map { "$it/**" })
                exclude(excludedPackages)
            },
            fileTree(layout.buildDirectory.dir("classes/kotlin/jvm/main")) {
                include(includedPackages.map { "$it/**" })
                exclude(excludedPackages)
            }
        )
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal() // 80% coverage
            }
        }
    }

    classDirectories.setFrom(
        files(
            fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
                include(includedPackages.map { "$it/**" })
                exclude(excludedPackages)
            },
            fileTree(layout.buildDirectory.dir("classes/java/main")) {
                include(includedPackages.map { "$it/**" })
                exclude(excludedPackages)
            },
            fileTree(layout.buildDirectory.dir("classes/kotlin/jvm/main")) {
                include(includedPackages.map { "$it/**" })
                exclude(excludedPackages)
            }
        )
    )
}
