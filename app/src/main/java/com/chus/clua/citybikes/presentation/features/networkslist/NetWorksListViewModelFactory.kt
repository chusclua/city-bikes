package com.chus.clua.citybikes.presentation.features.networkslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chus.clua.citybikes.domain.usecase.GetAllNetWorksUseCase
import com.chus.clua.citybikes.domain.usecase.GetNetWorksByCountryUseCase
import com.chus.clua.citybikes.presentation.mapper.NetWorkListUiModelMapper
import javax.inject.Inject

class NetWorksListViewModelFactory @Inject constructor(
    private val getNetWorksByCountryUseCase: GetNetWorksByCountryUseCase,
    private val getAllNetWorksUseCase: GetAllNetWorksUseCase
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = NetWorksListViewModel(
        getNetWorksByCountryUseCase, getAllNetWorksUseCase, NetWorkListUiModelMapper()
    ) as T
}