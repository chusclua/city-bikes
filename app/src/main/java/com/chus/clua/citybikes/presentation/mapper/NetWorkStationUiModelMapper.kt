package com.chus.clua.citybikes.presentation.mapper

import com.chus.clua.citybikes.domain.model.NetWorkStation
import com.chus.clua.citybikes.presentation.models.NetWorkStationUiModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

class NetWorkStationUiModelMapper : AbstractUiModelMapper<NetWorkStation, NetWorkStationUiModel>() {
    override fun mapFromDomain(from: NetWorkStation?) =
        NetWorkStationUiModel(
            id = from?.id,
            emptySlots = from?.emptySlots ?: 0,
            freeBikes = from?.freeBikes ?: 0,
            markerColor = from?.freeBikes?.let { mapAvailabilityColor(it) } ?: run { 0F },
            latLng = mapLatLng(from?.latitude, from?.longitude),
            name = from?.name,
            address = null
        )

    private fun mapAvailabilityColor(freeBikes: Int): Float {
        return when {
            freeBikes >= 10 -> BitmapDescriptorFactory.HUE_GREEN
            freeBikes in 5..9 -> BitmapDescriptorFactory.HUE_ORANGE
            else -> BitmapDescriptorFactory.HUE_RED
        }
    }

    private fun mapLatLng(latitude: Double?, longitude: Double?) =
        LatLng(latitude ?: 0.0, longitude ?: 0.0)
}