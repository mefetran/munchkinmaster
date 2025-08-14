package org.mefetran.munchkinmaster.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.mefetran.munchkinmaster.db.AppDatabase
import org.mefetran.munchkinmaster.db.PlayerDao
import org.mefetran.munchkinmaster.repository.DefaultPlayerRepository
import org.mefetran.munchkinmaster.repository.PlayerRepository

fun commonModule(): Module = module {
    single<PlayerDao> { get<AppDatabase>().getPlayerDao()}
}

fun repositoryModule(): Module = module {
    single<PlayerRepository> { DefaultPlayerRepository(get()) }
}

expect fun platformModule(): Module