package com.chus.clua.citybikes.presentation.features.map

sealed class MapEvent {
    data class LoadAllStations(val netWorkId: String?) : MapEvent()
    data class MarkerClick(val stationId: String?) : MapEvent()
}