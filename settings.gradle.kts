pluginManagement {
    repositories{
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
rootProject.name = "fitness-coach-app"
include(":app")
includeBuild("libs/KCountries") {
    dependencySubstitution {
        substitute(module("com.github.kimplify:countries-core")).using(project(":countries-core"))
        substitute(module("com.github.kimplify:countries-i18n")).using(project(":countries-i18n"))
    }
}
