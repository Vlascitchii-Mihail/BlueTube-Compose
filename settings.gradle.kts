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

rootProject.name = "BlueTubeCompose"
include(":app")
include(":data-remote")
include(":data-local")
include(":data-repository")
include(":domain")
include(":presentation-common")
include(":presentation-video-list")
include(":presentation-player")
include(":presentation-shorts")
include(":presentation-settings")
include(":common-test")
include(":common-test-android")
