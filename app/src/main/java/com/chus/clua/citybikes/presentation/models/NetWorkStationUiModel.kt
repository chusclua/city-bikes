package com.chus.clua.citybikes.presentation.models

import com.google.android.gms.maps.model.LatLng

data class NetWorkStationUiModel(
    val id: String?,
    val name: String?,
    val address: String?,
    val emptySlots: Int?,
    val freeBikes: Int?,
    val markerColor: Float?,
    val latLng: LatLng?
): UiModel