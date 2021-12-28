package com.chus.clua.citybikes.di.modules

import com.chus.clua.citybikes.BuildConfig
import com.chus.clua.citybikes.data.utils.AlwaysListTypAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val BASE = "base"

@Module
class NetworkModule {

    @Provides
    @Named(BASE)
    @Singleton
    fun provideBaseRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        buildRetrofit(gson, client, BuildConfig.BASE_URL)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().setLenient().serializeNulls().registerTypeAdapterFactory(
            AlwaysListTypAdapterFactory()
        ).create()


    private fun buildRetrofit(gson: Gson, client: OkHttpClient, baseUrl: String) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build()
}