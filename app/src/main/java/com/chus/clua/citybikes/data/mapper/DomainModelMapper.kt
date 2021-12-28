package com.chus.clua.citybikes.data.mapper

/**
 *
 * Interface for [RemoteModel] to [DomainModel] mappers.
 * It transforms API remote models into local data models
 *
 * @param <RM> the remote model input type
 * @param <DM> the entity model output type
 */
interface DomainModelMapper<in RM, out DM> {

    fun mapFromRemote(from: RM?): DM

    fun mapFromRemoteList(list: List<RM>?): List<DM>

}