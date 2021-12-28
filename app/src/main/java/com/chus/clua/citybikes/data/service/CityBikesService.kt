package com.chus.clua.citybikes.data.service

import javax.inject.Inject

class CityBikesService @Inject constructor(private val api: CityBikesApi) {
    fun getNetWorks() = api.getNetworks()
    fun getNetWorkById(networkId: String?) = api.getNetworkById(networkId)
}