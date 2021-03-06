package com.florencenjeri.waterreminder.di

import com.florencenjeri.waterreminder.ui.view.ProfileSettingsFragment
import com.florencenjeri.waterreminder.ui.viewModel.HomeViewModel
import com.florencenjeri.waterreminder.ui.viewModel.ProfileSettingsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presenterModule = module {
    scope<ProfileSettingsFragment> {
        viewModel {
            ProfileSettingsViewModel(get(), get(), get(), get())
        }
    }
    viewModel { HomeViewModel(get(), get(), get()) }
}