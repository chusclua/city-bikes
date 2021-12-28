package com.chus.clua.citybikes.data.repository

import com.chus.clua.citybikes.data.mapper.NetWorkDomainMapper
import com.chus.clua.citybikes.data.service.CityBikesService
import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.repository.NetworkRepository
import io.reactivex.Single


class NetworkRepositoryImp(
    private val service: CityBikesService,
    private val mapper: NetWorkDomainMapper
) : NetworkRepository {

    override fun getNetworks(): Single<List<NetWork>> {
        return service.getNetWorks().map { mapper.mapFromRemoteList(it.networks) }
    }

    override fun getNetworksByCountry(countryCode: String?): Single<List<NetWork>> {
        return getNetworks().map { list -> list filterByCountry countryCode }
    }

    override fun getNetworkById(networkId: String?): Single<NetWork> {
        return service.getNetWorkById(networkId).map { mapper.mapFromRemote(it.network) }
    }

    private infix fun List<NetWork>.filterByCountry(countryCode: String?) =
        this.filter { countryCode.equals(it.location?.country, ignoreCase = true) }

}