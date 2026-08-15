import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val appVersion = "0.5.0"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.detekt)
}


detekt {
    source.setFrom(
        files(
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/androidMain/kotlin",
            "src/androidUnitTest/kotlin",
            "src/desktopMain/kotlin",
            "src/desktopTest/kotlin",
        )
    )
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/config/detekt.yml")
}

kotlin {
    androidLibrary {
        namespace = "org.mefetran.munchkinmaster.composeLibrary"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        androidResources {
            enable = true
        }
        withHostTest {

        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.compose.navigationevent)
            implementation(libs.material.icons.extended)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.serialization)
            implementation(libs.decompose)
            implementation(libs.decompose.extensions.compose)
            implementation(libs.essenty.lifecycle)
            api(libs.datastore)
            api(libs.datastore.preferences)
            implementation(libs.room.runtime)
            implementation(projects.data)
            implementation(projects.domain)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.koin.test.junit4)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.ui.tooling)
}

compose.desktop {
    application {
        mainClass = "org.mefetran.munchkinmaster.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Msi, TargetFormat.Deb, TargetFormat.AppImage,
                TargetFormat.Rpm
            )
            packageName = "org.mefetran.munchkinmaster"
            packageVersion = appVersion
            linux {
                iconFile.set(project.file("../media/appicon/app_icon.png"))
            }
        }
    }
}