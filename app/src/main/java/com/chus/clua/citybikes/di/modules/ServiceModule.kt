package com.chus.clua.citybikes.di.modules

import com.chus.clua.citybikes.data.service.CityBikesApi
import com.chus.clua.citybikes.data.service.CityBikesService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideCityBikesService(api: CityBikesApi) = CityBikesService(api)
}