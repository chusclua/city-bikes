package com.chus.clua.citybikes.data.model

data class NetWorkRemoteModel (
    val id: String?,
    val name: String?,
    val company: List<String>?,
    val href: String?,
    val location: NetWorkLocationRemoteModel?,
    val stations: List<NetWorkStationRemoteModel>?
): RemoteModel