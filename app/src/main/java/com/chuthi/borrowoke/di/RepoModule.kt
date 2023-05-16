package com.chuthi.borrowoke.di

import com.chuthi.borrowoke.data.repo.ChatRepo
import com.chuthi.borrowoke.data.repo.DogRepo
import com.chuthi.borrowoke.data.repo.NewsRepo
import com.chuthi.borrowoke.data.repo.UserRepo
import org.koin.dsl.module

val repoModule = module {
    single { UserRepo(get()) }
    single { NewsRepo(get()) }
    single { DogRepo(get()) }
    single { ChatRepo(get()) }
}