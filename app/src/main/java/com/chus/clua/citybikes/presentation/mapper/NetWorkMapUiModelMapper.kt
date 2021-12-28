package com.chus.clua.citybikes.presentation.mapper

import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.model.NetWorkStation
import com.chus.clua.citybikes.presentation.models.NetWorkMapUiModel
import com.chus.clua.citybikes.presentation.models.NetWorkStationUiModel

class NetWorkMapUiModelMapper(
    val netWorkStationUiModelMapper: UiModelMapper<NetWorkStation, NetWorkStationUiModel>
) : AbstractUiModelMapper<NetWork, NetWorkMapUiModel>() {
    override fun mapFromDomain(from: NetWork?) =
        NetWorkMapUiModel(
            id = from?.id,
            name = from?.name,
            city = from?.location?.city,
            companies = from?.companyList?.joinToString(),
            stationUiModels = netWorkStationUiModelMapper.mapFromDomainList(from?.stations).reversed()
        )
}