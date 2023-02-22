package com.chuthi.borrowoke

import android.app.Application
import com.chuthi.borrowoke.di.viewModelModule
import com.chuthi.borrowoke.di.workerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@BaseApp)
            modules(
                listOf(
                    viewModelModule,
                    workerModule
                )
            )
        }
    }
}