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
include(":test_common:ui_test")
include(":test_common:unit_test")
include(":feature:common_ui")
include(":feature:player_screen")
include(":feature:settings_screen")
include(":feature:shorts_screen")
include(":feature:video_list_screen")
include(":data:local_source")
include(":data:remote_source")
include(":repository:repository_video")
include(":domain:domain_video")
