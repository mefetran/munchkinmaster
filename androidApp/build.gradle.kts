import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val appVersion = "0.5.0"

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    dependencies {
        implementation(projects.composeApp)
        implementation(libs.ui.tooling.preview)
        implementation(libs.androidx.activity.compose)
        implementation(libs.koin.android)
        implementation(libs.koin.androidx.compose)
        implementation(libs.decompose)
        implementation(libs.decompose.extensions.compose)
    }
}

android {
    namespace = "org.mefetran.munchkinmaster"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.mefetran.munchkinmaster"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = appVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
