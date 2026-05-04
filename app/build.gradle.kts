plugins {
    id("com.android.aplication")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.sebastianfiser.fitnesscoach"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sebastianfiser.fitnesscoach"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material3:material3:1.2.0")
}