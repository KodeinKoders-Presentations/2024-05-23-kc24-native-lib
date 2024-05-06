plugins {
    kotlin("multiplatform") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
    id("net.kodein.cup") version "1.0.0-Beta-01"
    id("net.kodein.gradle.resources.resource-files") version "1.0.0"
}

repositories {
    mavenCentral()
    google()
    mavenLocal()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/kodeinkoders/kodein-themes")
        content {
            includeGroup("net.kodein.themes")
        }
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")

            if (username == null || password == null) {
                println("WARNING: No GitHub credentials defined.")
                println("WARNING: Please add the gpr.user and gpr.key properties in your \$HOME/.gradle/gradle.properties.")
                println("WARNING: Do NOT add these properties into the project gradle.properties file (it should go in your HOME gradle.properties).")
                println("WARNING: The gpr.user and gpr.key properties must correspond to a Github Classic Personal Access Token.")
                println("WARNING: see https://docs.github.com/en/packages/learn-github-packages/about-permissions-for-github-packages#about-scopes-and-permissions-for-package-registries.")
            }
        }
    }
}

cup {
    targetDesktop()
    targetWeb()
}

kotlin {
    sourceSets.commonMain {
        dependencies {
            implementation(cup.sourceCode)
            implementation("net.kodein.themes:cup:2.0.0")

            implementation(compose.material)
            implementation("org.kodein.emoji:emoji-compose-m2:1.3.0")

            implementation("io.github.alexzhirkevich:qrose:1.0.1")
        }
        resources.srcDirs(resourceFiles.outputDir)
    }
}

tasks.withType<ProcessResources> {
    dependsOn(tasks.importResourceFiles)
}

dependencies {
    resourceFiles("net.kodein.themes:base-resources-font:2.0.0")
}