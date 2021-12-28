package com.chus.clua.citybikes.presentation.extension

import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Geocoder.countryCode(location: Location?): String? {
    location?.let { loc ->
        val addresses: List<Address>? = this.getFromLocation(loc.latitude, loc.longitude, 1)
        return addresses?.getOrNull(0)?.countryCode
    } ?: run { return null }
}

fun Geocoder.address(location: LatLng?): String? {
    location?.let { loc ->
        val addresses: List<Address>? = this.getFromLocation(loc.latitude, loc.longitude, 1)
        return addresses?.getOrNull(0)?.getAddressLine(0)
    } ?: run { return null }
}