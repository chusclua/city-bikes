package com.chus.clua.citybikes.data.service

import com.chus.clua.citybikes.data.model.NetWorkDetailRemoteModel
import com.chus.clua.citybikes.data.model.NetWorksRemoteModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

const val ID = "network_id"
const val NETWORKS = "v2/networks/"
const val NETWORK_ID = "v2/networks/{$ID}"

interface CityBikesApi {

    @GET(NETWORKS)
    fun getNetworks(): Single<NetWorksRemoteModel>

    @GET(NETWORK_ID)
    fun getNetworkById(@Path(ID) networkId: String?): Single<NetWorkDetailRemoteModel>

}