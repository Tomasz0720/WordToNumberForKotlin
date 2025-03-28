plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
}

group = "com.github.tomasz0720"
version = "0.1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/Tomasz0720/WordToNumberForKotlin")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
    
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    implementation("com.github.tomasz0720:wordtonumber:0.1.0")
}

kotlin {
    jvmToolchain(11) // Specify Java version compatibility
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Tomasz0720/WordToNumberForKotlin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "wordtonumber"
            version = project.version.toString()

            from(components["kotlin"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}