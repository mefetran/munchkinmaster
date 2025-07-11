package org.mefetran.munchkinmaster.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.mefetran.munchkinmaster.db.AppDatabase
import org.mefetran.munchkinmaster.db.getAppDatabase
import org.mefetran.munchkinmaster.getDatabaseBuilder

actual fun platformModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder()
        getAppDatabase(builder)
    }
}