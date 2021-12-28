package com.chus.clua.citybikes.presentation.features.map

import com.chus.clua.citybikes.presentation.models.NetWorkMapUiModel
import com.chus.clua.citybikes.presentation.models.NetWorkStationUiModel

sealed class MapViewState {
    object Loading : MapViewState()
    data class Success(val netWork: NetWorkMapUiModel?) : MapViewState()
    data class Error(val message: String?) : MapViewState()
    data class StationDetail(val stationUiModel: NetWorkStationUiModel?) : MapViewState()
}