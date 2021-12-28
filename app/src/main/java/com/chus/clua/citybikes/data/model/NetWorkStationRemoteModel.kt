package com.chus.clua.citybikes.data.model

import java.util.*

data class NetWorkStationRemoteModel(
    val id: String?,
    val name: String?,
    val free_bikes: Int?,
    val empty_slots: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Date?
): RemoteModel