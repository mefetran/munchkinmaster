package org.mefetran.munchkinmaster.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mefetran.munchkinmaster.data.db.AppDatabase
import org.mefetran.munchkinmaster.data.db.PlayerDao
import org.mefetran.munchkinmaster.data.repository.player.PlayerRepositoryImpl
import org.mefetran.munchkinmaster.data.storage.PlayerStorage
import org.mefetran.munchkinmaster.data.storage.room.RoomPlayerStorage
import org.mefetran.munchkinmaster.domain.repository.PlayerRepository
import org.mefetran.munchkinmaster.domain.usecase.dice.GetRandomDiceUseCase
import org.mefetran.munchkinmaster.domain.usecase.dice.GetRandomDiceUseCaseImpl
import org.mefetran.munchkinmaster.domain.usecase.player.CreatePlayerUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.CreatePlayerUseCaseImpl
import org.mefetran.munchkinmaster.domain.usecase.player.DeletePlayersByIdsUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.DeletePlayersByIdsUseCaseImpl
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayerByIdUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayerByIdUseCaseImpl
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayersUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.GetPlayersUseCaseImpl
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerUseCase
import org.mefetran.munchkinmaster.domain.usecase.player.UpdatePlayerUseCaseImpl

fun commonModule(): Module = module {
    single<PlayerDao> { get<AppDatabase>().getPlayerDao() }
}

fun repositoryModule(): Module = module {
    singleOf(::PlayerRepositoryImpl) { bind<PlayerRepository>() }
}

fun storageModule(): Module = module {
    singleOf(::RoomPlayerStorage) { bind<PlayerStorage>() }
}

fun useCaseModule(): Module = module {
    factoryOf(::CreatePlayerUseCaseImpl) { bind<CreatePlayerUseCase>() }
    factoryOf(::GetPlayerByIdUseCaseImpl) { bind<GetPlayerByIdUseCase>() }
    factoryOf(::UpdatePlayerUseCaseImpl) { bind<UpdatePlayerUseCase>() }
    factoryOf(::GetPlayersUseCaseImpl) { bind<GetPlayersUseCase>() }
    factoryOf(::DeletePlayersByIdsUseCaseImpl) { bind<DeletePlayersByIdsUseCase>() }
    factoryOf(::GetRandomDiceUseCaseImpl) { bind<GetRandomDiceUseCase>() }
}

expect fun platformModule(): Module
