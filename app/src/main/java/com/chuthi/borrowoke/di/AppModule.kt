package com.chuthi.borrowoke.di

import androidx.room.Room
import com.chuthi.borrowoke.BaseApp
import com.chuthi.borrowoke.BuildConfig
import com.chuthi.borrowoke.data.api.ApiService
import com.chuthi.borrowoke.data.api.ConnectivityInterceptor
import com.chuthi.borrowoke.data.api.HeaderInterceptor
import com.chuthi.borrowoke.data.database.AppDatabase
import com.chuthi.borrowoke.other.APP_DATABASE_NAME
import com.chuthi.borrowoke.other.BASE_URL
import com.chuthi.borrowoke.other.TIME_OUT
import com.chuthi.borrowoke.util.provideService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    // database
    single { provideAppDatabase(androidApplication() as BaseApp) }
    // okhttp
    single { provideOkHttpClient() }
    // retrofit
    single { provideRetrofit(get()) }
    // api service
    single { provideApiService(get()) }
    // dao
    single { provideUserDao(get()) }
}

fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(HeaderInterceptor())
        //.addInterceptor(ConnectivityInterceptor())
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
}


//
// Provider Methods
//

// region Service methods
fun provideApiService(retrofit: Retrofit) = provideService<ApiService>(retrofit)

// endregion
fun provideAppDatabase(app: BaseApp): AppDatabase = Room.databaseBuilder(
    app,
    AppDatabase::class.java,
    APP_DATABASE_NAME
).build()

fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()