package org.mefetran.munchkinmaster.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.mefetran.munchkinmaster.data.db.AppDatabase
import org.mefetran.munchkinmaster.data.db.PlayerDao
import org.mefetran.munchkinmaster.data.repository.player.PlayerRepositoryImpl
import org.mefetran.munchkinmaster.data.storage.PlayerStorage
import org.mefetran.munchkinmaster.data.storage.room.RoomPlayerStorage
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository

fun commonModule(): Module = module {
    single<PlayerDao> { get<AppDatabase>().getPlayerDao()}
}

fun repositoryModule(): Module = module {
    single<PlayerRepository> { PlayerRepositoryImpl(get()) }
}

fun storageModule(): Module = module {
    single<PlayerStorage> { RoomPlayerStorage(get()) }
}

expect fun platformModule(): Module