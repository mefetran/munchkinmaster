package org.mefetran.munchkinmaster.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.mefetran.munchkinmaster.data.db.AppDatabase
import org.mefetran.munchkinmaster.data.db.PlayerDao
import org.mefetran.munchkinmaster.data.repository.player.PlayerRepositoryImpl
import org.mefetran.munchkinmaster.data.storage.PlayerStorage
import org.mefetran.munchkinmaster.data.storage.room.RoomPlayerStorage
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.player.CreatePlayerUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.DeletePlayersByIdsUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayerByIdUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayersUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerUseCase

fun commonModule(): Module = module {
    single<PlayerDao> { get<AppDatabase>().getPlayerDao()}
}

fun repositoryModule(): Module = module {
    single<PlayerRepository> { PlayerRepositoryImpl(get()) }
}

fun storageModule(): Module = module {
    single<PlayerStorage> { RoomPlayerStorage(get()) }
}

fun useCaseModule(): Module = module {
    factory { CreatePlayerUseCase(get()) }
    factory { GetPlayerByIdUseCase(get()) }
    factory { UpdatePlayerUseCase(get()) }
    factory { GetPlayersUseCase(get()) }
    factory { DeletePlayersByIdsUseCase(get()) }
}

expect fun platformModule(): Module