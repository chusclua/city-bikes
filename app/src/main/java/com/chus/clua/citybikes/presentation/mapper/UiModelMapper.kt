package com.chus.clua.citybikes.presentation.mapper

/**
 *
 * Interface for [DomainModel] to [UiModel] mappers.
 * It transforms Domain models into Ui models
 *
 * @param <DM> the entity model input type
 * @param <UM> the ui model output type
 */
interface UiModelMapper<in DM, out UM> {

    fun mapFromDomain(from: DM?): UM

    fun mapFromDomainList(list: List<DM>?): List<UM>

}