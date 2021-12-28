package com.chus.clua.citybikes.domain.repository

import com.chus.clua.citybikes.domain.model.NetWork
import io.reactivex.Single

interface NetworkRepository {
    fun getNetworks(): Single<List<NetWork>>
    fun getNetworksByCountry(countryCode: String?): Single<List<NetWork>>
    fun getNetworkById(networkId: String?): Single<NetWork>
}