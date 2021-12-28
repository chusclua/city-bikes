package com.chus.clua.citybikes.di.modules

import com.chus.clua.citybikes.data.service.CityBikesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideCityBikesApi(@Named(BASE) retrofit: Retrofit): CityBikesApi = retrofit.create(CityBikesApi::class.java)
}