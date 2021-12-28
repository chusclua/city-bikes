package com.chus.clua.citybikes.data.mapper

import com.chus.clua.citybikes.data.model.NetWorkStationRemoteModel
import com.chus.clua.citybikes.domain.model.NetWorkStation

class NetWorkStationDomainMapper: AbstractDomainModelMapper<NetWorkStationRemoteModel, NetWorkStation>() {
    override fun mapFromRemote(from: NetWorkStationRemoteModel?) =
        NetWorkStation(
            emptySlots = from?.empty_slots,
            freeBikes = from?.free_bikes,
            id = from?.id,
            latitude = from?.latitude,
            longitude = from?.longitude,
            name = from?.name,
            timestamp = from?.timestamp
        )
}