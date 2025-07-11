package org.mefetran.munchkinmaster.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.mefetran.munchkinmaster.db.AppDatabase
import org.mefetran.munchkinmaster.db.PlayerDao

fun commonModule(): Module = module {
    single<PlayerDao> { get<AppDatabase>().getPlayerDao()}
}

expect fun platformModule(): Module