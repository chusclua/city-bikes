package com.chus.clua.citybikes.presentation.features.networkslist

import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel

sealed class NetWorkListState {
    object Loading: NetWorkListState()
    data class Success(val netWorks: List<NetWorkListUiModel>) : NetWorkListState()
    data class Error(val message: Any?): NetWorkListState()
}