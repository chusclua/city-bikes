package com.chus.clua.citybikes.data.mapper

import com.chus.clua.citybikes.data.model.RemoteModel
import com.chus.clua.citybikes.domain.model.DomainModel


abstract class AbstractDomainModelMapper<in RM, out DM> :
    DomainModelMapper<RM, DM> where RM : RemoteModel, DM : DomainModel {

    abstract override fun mapFromRemote(from: RM?): DM

    override fun mapFromRemoteList(list: List<RM>?): List<DM> {
        list?.let { return it.map { rm -> mapFromRemote(rm) } }
        return emptyList()
    }

}