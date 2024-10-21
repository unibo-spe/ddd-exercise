plugins {
    `java-library`
}

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }

    dependencies {
        // Use JUnit Jupiter for testing.
        testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}
