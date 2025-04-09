pluginManagement {
    repositories {
        gradlePluginPortal() // ✅ Must be first for Kotlin plugin
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Real Time Weather"
include(":app")
 