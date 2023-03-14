package com.chuthi.borrowoke.di

import androidx.room.Room
import com.chuthi.borrowoke.BaseApp
import com.chuthi.borrowoke.data.database.AppDatabase
import com.chuthi.borrowoke.other.APP_DATABASE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    // database
    single { provideAppDatabase(androidApplication() as BaseApp) }
    // dao
    single { provideUserDao(get()) }
}


//
// Provider Methods
//
fun provideAppDatabase(app: BaseApp): AppDatabase = Room.databaseBuilder(
    app,
    AppDatabase::class.java,
    APP_DATABASE_NAME
).build()

fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()