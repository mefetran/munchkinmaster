package org.mefetran.munchkinmaster.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(commonModule() + repositoryModule() + platformModule())
    }
}