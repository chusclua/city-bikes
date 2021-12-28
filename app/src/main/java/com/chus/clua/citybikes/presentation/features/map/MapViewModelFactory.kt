package com.chus.clua.citybikes.presentation.features.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chus.clua.citybikes.domain.usecase.GetNetWorkByIdUseCase
import com.chus.clua.citybikes.presentation.mapper.NetWorkMapUiModelMapper
import com.chus.clua.citybikes.presentation.mapper.NetWorkStationUiModelMapper
import javax.inject.Inject

class MapViewModelFactory @Inject constructor(
    private val useCase: GetNetWorkByIdUseCase
) : ViewModelProvider.NewInstanceFactory() {

    private val netWorkStationUiModelMapper = NetWorkStationUiModelMapper()
    private val netWorkMapUiModelMapper = NetWorkMapUiModelMapper(netWorkStationUiModelMapper)


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MapViewModel(
        useCase, netWorkMapUiModelMapper, netWorkStationUiModelMapper
    ) as T
}