plugins {
    kotlin("jvm") version "1.7.20"
    `java-library`
    `maven-publish`
}

group = "com.github.lebehn"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain{
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    val gprBaseUrl = "https://maven.pkg.github.com"
    val gprRepoOwner = "lebehn"
    val gprRepoId = "compose-utils"
    // Attempts to read the GPR user from local
    // project properties and then system environment variables.
    fun getGprUser(): String? {
        return if (project.hasProperty("gpr.user")) {
            project.properties["gpr.user"] as String?
        } else {
            System.getenv("GITHUB_USERNAME").toString()
        }
    }
    // Attempts to read the GPR key from local
    // project properties and then system environment variables.
    fun getGprKey(): String? {
        return if (project.hasProperty("gpr.key")) {
            project.properties["gpr.key"] as String?
        } else {
            System.getenv("GITHUB_TOKEN").toString()
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("$gprBaseUrl/$gprRepoOwner/$gprRepoId")
            credentials {
                username = getGprUser()
                password = getGprKey()
            }
        }
    }
    publications {
        register<MavenPublication>("compose-utils") {
            version = "0.1.0"
            groupId = "com.github.lebehn"
            from(components["java"])
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}