package com.chus.clua.citybikes.domain.model

import java.util.*

data class NetWorkStation(
    val id: String?,
    val name: String?,
    val emptySlots: Int?,
    val freeBikes: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Date?
) : DomainModel