package dev.soncresityindustries.schat.manager.di

import dev.soncresityindustries.schat.manager.ui.viewmodel.home.HomeViewModel
import dev.soncresityindustries.schat.manager.ui.viewmodel.installer.InstallerViewModel
import dev.soncresityindustries.schat.manager.ui.viewmodel.installer.LogViewerViewModel
import dev.soncresityindustries.schat.manager.ui.viewmodel.libraries.LibrariesViewModel
import dev.soncresityindustries.schat.manager.ui.viewmodel.settings.AdvancedSettingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    factoryOf(::InstallerViewModel)
    factoryOf(::AdvancedSettingsViewModel)
    factoryOf(::HomeViewModel)
    factoryOf(::LogViewerViewModel)
    factoryOf(::LibrariesViewModel)
}