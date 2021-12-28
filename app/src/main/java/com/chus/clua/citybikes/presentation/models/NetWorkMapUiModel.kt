package com.chus.clua.citybikes.presentation.models

data class NetWorkMapUiModel(
    val id: String?,
    val name: String?,
    val city: String?,
    val companies: String?,
    val stationUiModels: List<NetWorkStationUiModel>?
): UiModel