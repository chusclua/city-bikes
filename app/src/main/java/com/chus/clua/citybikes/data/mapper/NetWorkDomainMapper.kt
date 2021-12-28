package com.chus.clua.citybikes.data.mapper

import com.chus.clua.citybikes.data.model.NetWorkRemoteModel
import com.chus.clua.citybikes.domain.model.NetWork


class NetWorkDomainMapper(
    private val locationDomainMapper: NetWorkLocationDomainMapper,
    private val stationDomainMapper: NetWorkStationDomainMapper
) : AbstractDomainModelMapper<NetWorkRemoteModel, NetWork>() {
    override fun mapFromRemote(from: NetWorkRemoteModel?) =
        NetWork(
            id = from?.id,
            companyList = from?.company,
            location = locationDomainMapper.mapFromRemote(from?.location),
            name = from?.name,
            stations = stationDomainMapper.mapFromRemoteList(from?.stations)
        )

}