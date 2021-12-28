package com.chus.clua.citybikes.presentation.mapper

import com.chus.clua.citybikes.domain.model.DomainModel
import com.chus.clua.citybikes.presentation.models.UiModel


abstract class AbstractUiModelMapper<in DM, out UM> :
    UiModelMapper<DM, UM> where DM : DomainModel, UM : UiModel {

    abstract override fun mapFromDomain(from: DM?): UM

    override fun mapFromDomainList(list: List<DM>?): List<UM> {
        list?.let { return it.map { dm -> mapFromDomain(dm) } }
        return emptyList()
    }

}