package org.mefetran.munchkinmaster.domain.usecase

interface UseCase<Input, Output> {
    suspend fun execute(input: Input): Output
}