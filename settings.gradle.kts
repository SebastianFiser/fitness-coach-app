pluginManagement {
    repositories{
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
rootProject.name = "fitness-coach-app"
include(":app")
val kCountriesDir = "libs/KCountries"
if (kCountriesDir.exists())
    includeBuild("libs/KCountries") {
        dependencySubstitution {
            substitute(module("com.github.kimplify:countries-core")).using(project(":countries-core"))
            substitute(module("com.github.kimplify:countries-i18n")).using(project(":countries-i18n"))
        }
    }
