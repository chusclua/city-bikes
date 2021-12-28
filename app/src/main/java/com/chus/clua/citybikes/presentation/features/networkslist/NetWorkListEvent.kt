package com.chus.clua.citybikes.presentation.features.networkslist

import android.location.Location

sealed class NetWorkListEvent {
    object LoadAllNetWorks : NetWorkListEvent()
    data class LoadNearbyNetWorks(val countryCode: String?, val location: Location?) : NetWorkListEvent()
    data class FilterNetWorksByQuery(val query: String? = null) : NetWorkListEvent()
}