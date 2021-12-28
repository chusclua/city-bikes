package com.chus.clua.citybikes.presentation.utils

import android.location.Location
import com.chus.clua.citybikes.domain.model.NetWork
import java.util.Comparator

class ComparatorNetWorkByLocation(private val currentPosition: Location): Comparator<NetWork> {
    override fun compare(netWork1: NetWork?, netWork2: NetWork?): Int {
        val result1 = FloatArray(1)
        Location.distanceBetween(
            currentPosition.latitude,
            currentPosition.longitude,
            netWork1?.location?.latitude ?: 0.0,
            netWork1?.location?.longitude ?: 0.0,
            result1
        )
        val distance1 = result1[0]
        val result2 = FloatArray(1)
        Location.distanceBetween(
            currentPosition.latitude,
            currentPosition.longitude,
            netWork2?.location?.latitude ?: 0.0,
            netWork2?.location?.longitude ?: 0.0,
            result2
        )
        val distance2 = result2[0]
        return distance1.compareTo(distance2)
    }
}