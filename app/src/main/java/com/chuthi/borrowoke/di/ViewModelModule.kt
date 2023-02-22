package com.chuthi.borrowoke.di

import com.chuthi.borrowoke.ui.main.HomeViewModel
import com.chuthi.borrowoke.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Define view model module
 */
val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(), get()) }
}