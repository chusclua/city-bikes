package com.chus.clua.citybikes.presentation.models

data class NetWorkListUiModel(
    val id: String?,
    val name: String?,
    val city: String?,
    val country: String?,
    val query: String? = null
): UiModel