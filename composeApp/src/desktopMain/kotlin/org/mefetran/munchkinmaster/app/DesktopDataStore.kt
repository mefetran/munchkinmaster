package org.mefetran.munchkinmaster.app

import java.io.File

fun getDesktopDataStorePath(appName: String): String {
    val os = System.getProperty("os.name").lowercase()

    val baseDir = when {
        os.contains("mac") -> File(System.getProperty("user.home"), "Library/Application Support/$appName")
        os.contains("win") -> File(System.getenv("APPDATA"), appName)

        else -> File(System.getProperty("user.home"), ".config/$appName")
    }

    if (!baseDir.exists()) baseDir.mkdirs()

    return File(baseDir, DataStoreFileName).absolutePath
}
