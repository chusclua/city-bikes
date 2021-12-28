package com.chus.clua.citybikes.domain.model

data class NetWork(
    val id: String?,
    val name: String?,
    val location: NetWorkLocation?,
    val companyList: List<String>?,
    val stations: List<NetWorkStation>?
) : DomainModel