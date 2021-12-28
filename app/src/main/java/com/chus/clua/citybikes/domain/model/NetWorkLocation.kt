package com.chus.clua.citybikes.domain.model


data class NetWorkLocation (
    val city: String?,
    val country: String?,
    val latitude: Double?,
    val longitude: Double?
) : DomainModel