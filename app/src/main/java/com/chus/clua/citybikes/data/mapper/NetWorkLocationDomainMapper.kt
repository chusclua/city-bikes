package com.chus.clua.citybikes.data.mapper

import com.chus.clua.citybikes.data.model.NetWorkLocationRemoteModel
import com.chus.clua.citybikes.domain.model.NetWorkLocation

class NetWorkLocationDomainMapper :
    AbstractDomainModelMapper<NetWorkLocationRemoteModel, NetWorkLocation>() {
    override fun mapFromRemote(from: NetWorkLocationRemoteModel?) =
        NetWorkLocation(
            city = from?.city,
            country = from?.country,
            latitude = from?.latitude,
            longitude = from?.longitude
        )
}