package com.chuthi.borrowoke.di

import androidx.work.WorkManager
import com.chuthi.borrowoke.BaseApp
import com.chuthi.borrowoke.woker.BlurWorker
import com.chuthi.borrowoke.woker.MyWorker
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker { BlurWorker(get(), get()) }
    single { provideWorkManager(androidApplication() as BaseApp) }
    single { provideMyWorker(get()) }
}

fun provideWorkManager(app: BaseApp): WorkManager = WorkManager.getInstance(app.applicationContext)

fun provideMyWorker(workManager: WorkManager) = MyWorker(workManager)