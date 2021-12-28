package com.chus.clua.citybikes.data.model


data class NetWorkLocationRemoteModel (
    val city: String?,
    val country: String?,
    val latitude: Double?,
    val longitude: Double?
) : RemoteModel