package org.mefetran.munchkinmaster

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform