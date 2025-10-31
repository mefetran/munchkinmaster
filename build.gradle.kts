plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.roomGradlePlugin) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

// After updating the Compose dependency,
// the Skiko cache sometimes conflicts with the new Compose version.
// Therefore, we clean the Skiko cache after every clean task.
tasks.register("clean") {
    group = "build"
    description = "Cleans composeApp and Skiko cache"

    dependsOn(":composeApp:clean")

    doLast {
        val skikoCacheDir = File(System.getProperty("user.home"), ".skiko")

        if (skikoCacheDir.exists()) {
            println("Removing Skiko cache: ${skikoCacheDir.absolutePath}")
            skikoCacheDir.deleteRecursively()
            println("Skiko cache removed")
        } else {
            println("No Skiko cache found — nothing to clean")
        }
    }
}