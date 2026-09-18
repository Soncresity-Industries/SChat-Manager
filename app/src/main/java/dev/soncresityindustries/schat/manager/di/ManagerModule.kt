package dev.soncresityindustries.schat.manager.di

import dev.soncresityindustries.schat.manager.domain.manager.DownloadManager
import dev.soncresityindustries.schat.manager.domain.manager.InstallManager
import dev.soncresityindustries.schat.manager.domain.manager.PreferenceManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerModule = module {
    singleOf(::DownloadManager)
    singleOf(::PreferenceManager)
    singleOf(::InstallManager)
}