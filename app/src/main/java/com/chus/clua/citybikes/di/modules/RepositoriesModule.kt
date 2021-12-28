package com.chus.clua.citybikes.di.modules

import com.chus.clua.citybikes.data.mapper.NetWorkDomainMapper
import com.chus.clua.citybikes.data.mapper.NetWorkLocationDomainMapper
import com.chus.clua.citybikes.data.mapper.NetWorkStationDomainMapper
import com.chus.clua.citybikes.data.service.CityBikesService
import com.chus.clua.citybikes.domain.repository.NetworkRepository
import com.chus.clua.citybikes.data.repository.NetworkRepositoryImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun providesNetWorkRepository(service: CityBikesService): NetworkRepository = NetworkRepositoryImp(service, mapper())

    private fun mapper() = NetWorkDomainMapper(NetWorkLocationDomainMapper(), NetWorkStationDomainMapper())
}