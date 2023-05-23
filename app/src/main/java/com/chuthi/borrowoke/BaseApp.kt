package com.chuthi.borrowoke

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.chuthi.borrowoke.di.appModule
import com.chuthi.borrowoke.di.repoModule
import com.chuthi.borrowoke.di.viewModelModule
import com.chuthi.borrowoke.di.workerModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@BaseApp)
            workManagerFactory()
            modules(
                listOf(
                    appModule,
                    repoModule,
                    viewModelModule,
                    workerModule,
                )
            )
        }
    }

    /**
     * Check is network available or not
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities?.run {
            return when {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return false
    }

    companion object {
        lateinit var instance: BaseApp
            private set
    }
}