package dev.soncresityindustries.schat.manager.di

import dev.soncresityindustries.schat.manager.domain.repository.RestRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::RestRepository)
}