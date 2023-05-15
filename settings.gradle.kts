pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

rootProject.name = "armeria-issues-202305"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":example:reactive-condition-issue")
include(":example:reactor-hooks-issue")
include(":protocol")
