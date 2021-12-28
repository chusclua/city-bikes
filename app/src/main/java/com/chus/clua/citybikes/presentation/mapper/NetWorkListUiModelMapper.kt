package com.chus.clua.citybikes.presentation.mapper

import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel

class NetWorkListUiModelMapper: AbstractUiModelMapper<NetWork, NetWorkListUiModel>() {
    override fun mapFromDomain(from: NetWork?) =
        NetWorkListUiModel(
            id = from?.id,
            name = from?.name,
            city = from?.location?.city,
            country = from?.location?.country
        )
}