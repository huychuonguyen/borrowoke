package com.chuthi.borrowoke.di

import com.chuthi.borrowoke.ui.animate.AnimationViewModel
import com.chuthi.borrowoke.ui.chat.ChatViewModel
import com.chuthi.borrowoke.ui.dog.DogViewModel
import com.chuthi.borrowoke.ui.home.HomeViewModel
import com.chuthi.borrowoke.ui.main.MainViewModel
import com.chuthi.borrowoke.ui.news.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Define view model module
 */
val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { AnimationViewModel(get()) }
    viewModel { DogViewModel(get()) }
    viewModel { ChatViewModel(get(), get()) }
}