import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

android {
    namespace = "com.sebastianfiser.fitnesscoach"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        applicationId = "com.sebastianfiser.fitnesscoach"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        val localProps = Properties()
        localProps.load(file("../local.properities").inputStream())

        buildConfigField("String", "APPWRITE_ENDPOINT", "\"${localProps["APPWRITE_ENDPOINT"]}\"")
        buildConfigField("String", "APPWRITE_PROJECT_ID", "\"${localProps["APPWRITE_PROJECT_ID"]}\"")
    }

    signingConfigs {
        create("release") {
            val localProps = Properties()
            localProps.load(file("../local.properities").inputStream())
            storeFile = file((localProps["KEYSTORE_PATH"] as? String) ?: "dummy.keystore")
            storePassword = ((localProps["KEYSTORE_PASSWORD"] as? String) ?: "")
            keyAlias = ((localProps["KEY_ALIAS"] as? String) ?: "")
            keyPassword = ((localProps["KEY_PASSWORD"] as? String) ?: "")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("io.appwrite:sdk-for-android:5.1.0")
    implementation("org.kimplify:countries-core:0.1.1")
    implementation("org.kimplify:countries-i18n:0.1.1")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:2.3.0")
}
