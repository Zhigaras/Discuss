pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Discuss"
include(":app")
include(":auth")
include(":feature:login")
include(":core")
include(":feature:home")
include(":cloudservice")
include(":feature:calls")
